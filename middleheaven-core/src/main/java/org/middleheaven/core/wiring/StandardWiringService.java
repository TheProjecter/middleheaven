package org.middleheaven.core.wiring;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.annotations.Component;
import org.middleheaven.core.annotations.Service;
import org.middleheaven.core.annotations.Shared;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.RegistryServiceContext;
import org.middleheaven.core.services.ServiceScope;
import org.middleheaven.events.EventListenersSet;
import org.middleheaven.graph.DirectGraph;
import org.middleheaven.logging.Logger;
import org.middleheaven.util.collections.ClassMap;
import org.middleheaven.util.function.Predicate;

@Service
public class StandardWiringService implements WiringService {

	protected static final Introspector Instrospector = null;

	private final Collection<WiringItemBundle> bundles = new CopyOnWriteArrayList<WiringItemBundle>();


	private final BinderImpl binder = new BinderImpl();
	private final List<WiringInterceptor> interceptors = new CopyOnWriteArrayList<WiringInterceptor>();
	private final Map<String, Scope> scopes = new LinkedHashMap<String, Scope>();

	private NewInstanceScope newInstanceScope = new NewInstanceScope();
	private SharedScope sharedScope = new SharedScope();

	private final ProfilesBag activeProfiles = new ProfilesBag();

	private final PropertyResolver propertyResolver;

	public StandardWiringService(PropertyManagers propertyManagers, RegistryServiceContext serviceRegistryContext) {
		parsers.add(new DefaultWiringModelParser());

		propertyResolver = new PropertyResolver(propertyManagers);

		ClassSet contextClasses = new ClassSet();

		contextClasses.add(Logger.class.getPackage());

		this.addItemBundle(new ClassSetWiringBundle(contextClasses));


		registerScope("service", new ServiceScope(serviceRegistryContext));
		registerScope("shared", sharedScope);
		registerScope("default", newInstanceScope);

		// register scopes in shared scope.
		//		binder.bind(SharedScope.class).in(Shared.class).toInstance(sharedScope);
		//		binder.bind(NewInstanceScope.class).in(Shared.class).toInstance(newInstanceScope);
		//		binder.bind(ServiceScope.class).in(Shared.class);

		// add connector

		if (ClassIntrospector.isInClasspath("javax.annotation.Resource")
				&& ClassIntrospector
				.isInClasspath("org.middleheaven.core.wiring.connectors.JavaEE5InjectonConnector")) {
			this.addConnector((WiringConnector) ClassIntrospector
					.loadFrom(
							"org.middleheaven.core.wiring.connectors.JavaEE5InjectonConnector")
							.newInstance());
		}

	}

	public void close(){
		for (Scope scopePool : scopes.values()){
			scopePool.clear();
		}
	}

	private BindingMap bindingMap = new BindingMap();

	ClassMap<BeanDependencyModel> models = new ClassMap<BeanDependencyModel>();
	Set<WiringModelReader> parsers = new CopyOnWriteArraySet<WiringModelReader>();

	private BeanDependencyModel revolveBeanModel(Class<?> type) {

		BeanDependencyModel model = models.get(type);

		if (model == null) {
			model = new BeanDependencyModel(type);

			models.put(type, model);

			if (String.class.isAssignableFrom(type)
					|| Number.class.isAssignableFrom(type)) {
				return model;
			}

			for (WiringModelReader reader : this.parsers) {
				reader.readBeanModel(type, model);
			}
		}

		return model;
	}

	private class BinderImpl implements Binder, EditableBinder,
	ConnectableBinder {


		public BinderImpl() {


		}

		@Override
		public <T> BindingBuilder<T> bind(Class<T> type) {
			return new StandardBindingBuilder<T>(this, type);
		}

		@Override
		public <T> PropertyBindingBuilder<T> bindProperty(Class<T> type) {
			Binding binding = new Binding();
			binding.setSourceType(type);
			binding.setResolver(propertyResolver);

			PropertyBindingBuilder<T> b = new PropertyBindingBuilder<T>(this,
					binding);
			binder.addBinding(binding);

			return b;
		}

		@Override
		public void addBinding(Binding binding) {

			if (getActiveProfiles().accepts(binding.getProfiles())){
				bindingMap.add(binding);
			} else {
				Logger.onBook("binding").warn("Discarded binding {0} beacause does not match active profiles", binding);
			}
		}


		public BeanDependencyModel getBeanModel(Object object) {
			return getBeanModel(object.getClass());
		}

		@Override
		public BeanDependencyModel getBeanModel(Class<?> type) {
			return revolveBeanModel(type);
		}

		@Override
		public void addWiringModelParser(WiringModelReader parser) {
			parsers.add(parser);
		}

		@Override
		public void removeWiringModelParser(WiringModelReader parser) {
			parsers.remove(parser);
		}

		@Override
		public Object getInstance(WiringQuery query) {

			return factory.getInstance(query);

			//			// specialcase of wiringTarget injection
			//
			//			if (query.getContract().equals(WiringTarget.class)){
			//				return query.getTarget();
			//			}
			//
			//			Key key = Key.keyFor(query.getContract(), query.getParams());
			//
			//			Binding binding = bindingMap.findNearest(query);
			//			if (binding == null) {
			//				// is binding is not found try to guess one automatically
			//
			//	
			//				
			//				// if its a concrete class create an auto-binding for it
			//				if (!query.getContract().isAnnotation()
			//						&& !query.getContract().isInterface()
			//						&& !Modifier.isAbstract(query.getContract()
			//								.getModifiers())) {
			//
			//					BeanDependencyModel model = this.getBeanModel(query.getContract());
			//
			//					binding = new Binding();
			//
			//					binding.addParams(model.getParams());
			//					binding.setScope("shared");
			//
			//					binding.setSourceType(query.getContract());
			//
			//					binding.setResolver(FactoryResolver.instanceFor(model));
			//
			//					binding.setProfiles(model.getProfiles());
			//
			//					this.addBinding(binding);
			//
			//					return getInstance(query); // repeat search recursively
			//				}
			//
			//				// try understand from which scope could be read
			//				Set<Annotation> annotations = Introspector
			//						.of(query.getContract()).inspect().annotations()
			//						.retrive();
			//
			//				// find a scope annotation
			//				String found = null;
			//				for (Annotation a : annotations) {
			//					final String scopeName = WiringUtils.readScope(a);
			//					if (scopes.containsKey(scopeName)) {
			//						if (found != null) {
			//							// already found one previouslly
			//							throw new BindingException(
			//									"To many auto-binding options");
			//						}
			//						found = scopeName; // do not break
			//					}
			//				}
			//
			//				if (found == null && query.isRequired()) {
			//					throw new CanSatisfyDependencyException(query.getContract(), query.getTarget());
			//				} else {
			//					return getOrCreate(query, key,  found , null);
			//				}
			//
			//			}
			//
			//
			//			// binding was found
			//			// resolve target
			//			if (stack.contains(key)) {
			//				// cyclic reference
			//				// return proxy
			//				CyclicProxy proxy = cyclicProxies.get(key);
			//				if (proxy == null) {
			//					proxy = new CyclicProxy();
			//					cyclicProxies.put(key, proxy);
			//				}
			//				return Introspector.of(query.getContract()).newProxyInstance(proxy);
			//
			//			} else {
			//				stack.offer(key);
			//				try {
			//					return getOrCreate(query, key,  binding.getScope(),binding.getResolver());
			//				} catch (RuntimeException e) {
			//					stack.remove(key);
			//					throw e;
			//				}
			//			}

		}
//
//		private Object getOrCreate(WiringQuery query, Key key,String scopeName, Resolver resolver ) {
//			// resolve scope
//
//			Scope scopePool = getScopePool(scopeName);
//
//			if (resolver == null){
//
//				if (!query.getContract().isInterface()) {
//					resolver = FactoryResolver.instanceFor(this.getBeanModel(query.getContract()));
//				} else {
//					resolver = NullResolver.instance();
//				}
//
//			}
//
//			if (resolver != null) {
//				// attach interceptors
//
//				ResolutionContext context = new StandardResolutionContext(scopeName);
//
//				final InterceptorResolver interceptorResolver = new InterceptorResolver(interceptors, resolver);
//				Object obj = scopePool.getInScope(context, query, interceptorResolver);
//
//				stack.remove(key);
//
//				// check if there is any proxy to complete
//				CyclicProxy proxy = cyclicProxies.get(key);
//				if (proxy != null) {
//					proxy.setRealObject(obj);
//				}
//				return obj;
//			} else {
//				return null;
//			}
//		}

		public void wireMembers(Object obj) {
			BeanDependencyModel model = this.getBeanModel(obj.getClass());

			for (AfterWiringPoint a : model.getAfterPoints()) {
				obj = a.writeAtPoint(factory, obj);
			}
		}

		@Override
		public void removeBinding(Binding binding) {
			bindingMap.remove(binding);

		}

		public Scope getScopePool(String scopeName ) {

			// find scope class
			Scope scopePool = scopes.get(scopeName);

			if (scopePool == null) {
				throw new BindingScopeNotFoundException(scopeName);

			}

			return scopePool;
		}

		@Override
		public void addInterceptor(WiringInterceptor interceptor) {
			interceptors.add(interceptor);
		}

		@Override
		public void removeInterceptor(WiringInterceptor interceptor) {
			interceptors.remove(interceptor);
		}

		/**
		 * @param beanModel
		 */
		public void addBindings(BeanDependencyModel model, Resolver resolver) {

			for (String scope : model.getScopes()) {

				for (Class<?> contract : model.getContractTypes()){
					Binding binding = new Binding();

					binding.addParams(model.getParams());
					binding.setScope(scope);
					binding.setProfiles(model.getProfiles());

					binding.setSourceType(contract);

					binding.setResolver(resolver);

					this.addBinding(binding);

					initializeBinding(binding);
				}

			}

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void autobind(ClassSet set) {
			
			for (Class c : set){
				if (c.getEnclosingClass() == null){ // no inner classes
					this.bind(c).inSharedScope().to(c);
				}
			}
		}

	}

	public class StandardResolutionContext implements ResolutionContext {

		private String scopeName;


		protected StandardResolutionContext(String scopeName) {
			super();
			this.scopeName = scopeName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getScopeName() {
			return scopeName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public InstanceFactory getInstanceFactory() {
			return factory;
		}

	}

	private final EventListenersSet<ObjectPoolListener> objetEvent = EventListenersSet
			.newSet(ObjectPoolListener.class);


	protected void initializedAll(){
		for (Binding b : bindingMap.getBindings()) {
			initializeBinding(b);
		}
	}

	private void initializeBinding(Binding b) {
		if (!b.isInicialized()
				&& !b.isLazy()
				&& ("service".equals(b.getScope()) || "shared".equals(b.getScope()))) {
			try {
				getInstance(b.getSourceType());
				b.setInicialized(true);
			} catch (Exception e){
				//no-op. eager loading fail. try lazy
			}
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public WiringService addConfiguration(BindConfiguration... modules) {
		for (BindConfiguration m : modules) {
			m.configure(binder);
		}

		initializedAll();

		return this;
	}

	@Override
	public <T> T getInstance(Class<T> type) {
		return type.cast(binder.getInstance(WiringQuery.search(type)));
	}

	@Override
	public <T> T getInstance(Class<T> type,
			Map<String, ? extends Object> params) {

		@SuppressWarnings("unchecked")
		Map<String, Object> p = (Map<String, Object>) params;

		return type.cast(binder.getInstance(WiringQuery.search(type, p)));
	}

	@Override
	public void wireMembers(Object obj) {
		binder.wireMembers(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addObjectCycleListener(ObjectPoolListener listener) {
		objetEvent.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeObjectCycleListener(ObjectPoolListener listener) {
		objetEvent.removeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProfilesBag getActiveProfiles() {
		return activeProfiles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerScope(String name, Scope scope) {
		scopes.put(name, scope);

		scope.addScopeListener(new ScopeListener() {

			@Override
			public void onObjectAdded(ScopePoolEvent scopePoolEvent) {
				objetEvent.broadcastEvent()
				.onObjectAdded(
						new ObjectEvent(scopePoolEvent
								.getObject()));
			}

			@Override
			public void onObjectRemoved(ScopePoolEvent scopePoolEvent) {
				objetEvent.broadcastEvent()
				.onObjectRemoved(
						new ObjectEvent(scopePoolEvent
								.getObject()));
			}

		});

	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void addConnector(WiringConnector  connector) {
		connector.connect(binder);

	}

	Predicate<Class> componentFilter = new Predicate<Class>(){

		@Override
		public Boolean apply(Class obj) {
			return Instrospector.of(obj).isAnnotationPresent(Component.class);
		}

	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh() {

		Queue<BeanDependencyModel> wiringInterceptors = new LinkedList<BeanDependencyModel>();

		for (WiringItemBundle bundle : this.bundles){

			for (WiringItem item : bundle) {

				if (item.isType()) {

					Class<?> type = (Class<?>) item.getItem();

					// if is component
					if (componentFilter.apply(type).booleanValue()){
						BeanDependencyModel model = this.binder.getBeanModel(type);

						processDependableBean(null, null, model, FactoryResolver.instanceFor(model));

						if (Introspector.of(model.getBeanClass()).isSubtypeOf(WiringInterceptor.class)){
							wiringInterceptors.add(model);
						}
					}
				} else {
					// instance
					Object instance = item.getItem();

					BeanDependencyModel model = this.binder.getBeanModel(instance.getClass());

					final InstanceResolver resolver = new InstanceResolver(instance);

					processDependableBean(null, null, model, resolver);

					if (Introspector.of(model.getBeanClass()).isSubtypeOf(WiringInterceptor.class)){
						wiringInterceptors.add(model);
					}

					//binder.addBindings(model, resolver);
				}
			}

		}
		
		// inicialize wiring interceptors

		BeanDependencyModel im;

		while ((im = wiringInterceptors.poll()) != null){
			try {
				WiringInterceptor obj = WiringInterceptor.class.cast( binder.getInstance(WiringQuery.search(im.getBeanClass())));
				this.interceptors.add((WiringInterceptor) obj);
			} catch (BindingException e){
				wiringInterceptors.add(im);
			}
		}

		// inicialize shared not lazy

		for (Binding binding : bindingMap.getBindings()){
			if (!binding.isInicialized() && binding.getScope().equals("shared") && !binding.isLazy()){
				binder.getInstance(WiringQuery.search(binding.getSourceType(), binding.getParams()));

				binding.setInicialized(true);
			}
		}

	}



	private void processDependableBean(
			final Map<String, DependendableBean> beans,
			final DirectGraph<DependencyRelation, Class<?>> graph,
			BeanDependencyModel model,
			Resolver resolver) {

		if (activeProfiles.accepts(model.getProfiles())){

			final Collection<DependendableBean> allbeans = DependendableBean.fromModel(model, resolver);


			for (DependendableBean bean : allbeans){
				Binding binding = new Binding();
				binding.setSourceType(bean.getType());
				binding.setResolver(bean.getResolver());
				binding.setScope(bean.getScope());

				binder.addBinding(binding);
			}



			if (!model.getPublishPoints().isEmpty()){

				for (PublishPoint pp : model.getPublishPoints()){

					BeanDependencyModel createdBeanModel = this.binder.getBeanModel(pp.getPublishedType());

					final Collection<DependendableBean> publishedBeans = DependendableBean.fromPublishing(createdBeanModel, pp, model.getBeanClass());

					for (DependendableBean bean : publishedBeans){
						Binding binding = new Binding();
						binding.setSourceType(bean.getType());
						binding.setResolver(bean.getResolver());
						binding.setScope(bean.getScope());
						
						binder.addBinding(binding);
					}
				}

			}

//			// treat interfaces as publishpoints
//
//			for (Class i : Instrospector.of(model.getBeanClass()).getDeclaredInterfaces()){
//				BeanDependencyModel interfaceBean = this.binder.getBeanModel(i);
//
//				final DependendableBean iBean = DependendableBean.fromImplementation(interfaceBean, bean);
//
//
//				Binding binding2 = new Binding();
//				binding2.setSourceType(iBean.getType());
//				binding2.setResolver(iBean.getResolver());
//				binding2.setScope(iBean.getScope());
//
//				binder.addBinding(binding2);
//
//			}

		}
	}

	StandardInstanceFactory factory = new StandardInstanceFactory();
	Queue<Key> stack = new LinkedList<Key>();
	Map<Key, CyclicProxy> cyclicProxies = new HashMap<Key, CyclicProxy>();

	public class StandardInstanceFactory implements InstanceFactory{



		public void addInstance(DependendableBean bean) {


			final String scopeName = bean.getScope();

			Scope pool = scopes.get(scopeName);


			ResolutionContext context = new StandardResolutionContext(scopeName){
				/**
				 * {@inheritDoc}
				 */
				@Override
				public InstanceFactory getInstanceFactory() {
					return StandardInstanceFactory.this;
				}
			};



			final InterceptorResolver interceptorResolver = new InterceptorResolver(interceptors, bean.getResolver());

			pool.getInScope(context, WiringQuery.search(bean.getType()), interceptorResolver);


		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getInstance(final WiringQuery query) {

			if (query.getContract().equals(WiringTarget.class)){
				return WiringTarget.class.cast(query.getTarget());
			}



			Binding binding = bindingMap.findNearest(query);
			if (binding == null) {
				if (query.getContract().isInterface()){
					// search in service scope
					Scope scope = scopes.get("service");
				
					ResolutionContext context = new StandardResolutionContext("service"){
						/**
						 * {@inheritDoc}
						 */
						@Override
						public InstanceFactory getInstanceFactory() {
							return factory;
						}
					};
					
					Object service= scope.getInScope(context, query, NullResolver.instance());
					
					if (service != null){
						// there is a service for this interface
						// create binding for future reference
						// TODO create binding when registring service
						
						binding = new Binding();
						
						binding.setInicialized(true);
						binding.setLazy(false);
						binding.setScope("service");
						binding.setSourceType(query.getContract());
						binding.setResolver(NullResolver.instance());
						
						bindingMap.add(binding);
					}
					
					return query.getContract().cast(service);
				} else if (!Modifier.isAbstract(query.getContract().getModifiers())) {
					
					
						binder.bind(query.getContract())
						.inSharedScope()
						.to(query.getContract());
						
						return this.getInstance(query);
					
				} else {
					throw new BindingException("Can not satisfy binding: " + query);
				}
				
				
			} else {


				Key key = Key.keyFor(query.getContract(), query.getParams());

				// binding was found
				// resolve target
				if (!stack.isEmpty() && stack.contains(key)) {
					// cyclic reference

					if (query.getContract().equals(String.class)){
						throw new BindingException("Propery must be bound for String injection");
					} else if (Modifier.isFinal(query.getContract().getModifiers())){
						throw new BindingException("A cycle was detected up on a final type " + query.getContract() + ". Cycle can not be resolved.");
					}

					
					if (query.getContract().isInterface()){
						// return proxy
						CyclicProxy proxy = cyclicProxies.get(key);
						if (proxy == null) {
							proxy = new CyclicProxy();
							cyclicProxies.put(key, proxy);
						}
						return Introspector.of(query.getContract()).newProxyInstance(proxy);

					} else {
						// TODO register as self-binding 
						return Introspector.of(query.getContract()).newInstance();
					}
					
				} else {
					stack.offer(key);
					try {



						Scope scope = scopes.get(binding.getScope());

						ResolutionContext context = new StandardResolutionContext(binding.getScope()){
							/**
							 * {@inheritDoc}
							 */
							@Override
							public InstanceFactory getInstanceFactory() {
								return factory;
							}
						};


						//FactoryResolver.instanceFor(revolveBeanModel(query.getContract())
						final InterceptorResolver interceptorResolver = new InterceptorResolver(interceptors, binding.getResolver());


						Object obj = scope.getInScope(context, query, interceptorResolver);


						// check if there is any proxy to complete
						CyclicProxy proxy = cyclicProxies.get(key);
						if (proxy != null) {
							proxy.setRealObject(obj);
						}

						binding.setInicialized(true);


						return obj;



					} catch (RuntimeException e) {
						throw e;
					} finally {
						stack.remove(key);
					}
				}




			} 

		}


	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public WiringService addItemBundle(WiringItemBundle bundle) {
		this.bundles.add(bundle);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WiringService addItem(WiringItem item) {
		this.bundles.add(new EditableWiringItemBundle().add(item));
		return this;
	}
}
