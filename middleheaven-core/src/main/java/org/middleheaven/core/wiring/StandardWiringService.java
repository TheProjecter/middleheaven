package org.middleheaven.core.wiring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.collections.ClassMap;
import org.middleheaven.core.annotations.Component;
import org.middleheaven.core.annotations.Service;
import org.middleheaven.core.bootstrap.RegistryServiceContext;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.ServiceScope;
import org.middleheaven.events.EventListenersSet;
import org.middleheaven.graph.DirectGraph;
import org.middleheaven.logging.Logger;
import org.middleheaven.util.function.Maybe;
import org.middleheaven.util.function.Predicate;

@Service
public class StandardWiringService implements WiringService {

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

		}


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

	private class StandardResolutionContext implements ResolutionContext {

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
	public WiringService addConfiguration(BindConfiguration... configuration) {
		for (BindConfiguration m : configuration) {
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

		scope.addScopeListener(new SimpleScopeListener(objetEvent));

	}
	
	public static class SimpleScopeListener implements ScopeListener{

		private EventListenersSet<ObjectPoolListener> eventSet;
		
		
		public SimpleScopeListener(
				EventListenersSet<ObjectPoolListener> objetEvent) {
			super();
			this.eventSet = objetEvent;
		}

		@Override
		public void onObjectAdded(ScopePoolEvent scopePoolEvent) {
			eventSet.broadcastEvent()
			.onObjectAdded(
					new ObjectEvent(scopePoolEvent
							.getObject()));
		}

		@Override
		public void onObjectRemoved(ScopePoolEvent scopePoolEvent) {
			eventSet.broadcastEvent()
			.onObjectRemoved(
					new ObjectEvent(scopePoolEvent
							.getObject()));
		}

		
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void addConnector(WiringConnector  connector) {
		connector.connect(binder);

	}

	private static final Predicate<Class> componentFilter = new IntrospectorPredicate();
			

	
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
	Deque<Key> stack = new LinkedList<Key>();
	Map<Key, CyclicProxy> cyclicProxies = new HashMap<Key, CyclicProxy>();

	public class StandardInstanceFactory implements InstanceFactory{


		/**
		 * {@inheritDoc}
		 */
		@Override
		public Maybe<Object> peekCyclickProxy(Class<?> contract) {

			Key key = Key.keyFor(contract, Collections.<String,Object>emptyMap());

			if (!stack.isEmpty() && stack.contains(key) && !stack.peekLast().equals(key)) {
				return Maybe.of(obtainCyclicProxy(contract, key));
			}

			return Maybe.absent();
		}


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



			Binding binding = bindingMap.findNearestWithParams(query);
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

					return obtainCyclicProxy(query.getContract(), key);

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

					} finally {
						stack.remove(key);
					}
				}




			} 

		}


		private Object obtainCyclicProxy(Class contract, Key key) {

			if (contract.equals(String.class)){
				throw new BindingException("Propery must be bound for String injection");
			} else if (Modifier.isFinal(contract.getModifiers())){
				throw new BindingException("A cycle was detected up on a final type " + contract + ". Cycle can not be resolved.");
			}

			final ClassIntrospector introspector = Introspector.of(contract);

			if (contract.isInterface()){
				// return proxy
				CyclicProxy proxy = cyclicProxies.get(key);
				if (proxy == null) {
					proxy = new CyclicProxy();
					cyclicProxies.put(key, proxy);
				}
				return introspector.newProxyInstance(proxy);

			} else {


				Constructor c = (Constructor) introspector.inspect().constructors().sortedByQuantityOfParameters().retrive();

				Object[] params = new Object[c.getParameterTypes().length];
				for (int i =0; i < params.length; i++){
					params[i] = getInstance(WiringQuery.search(c.getParameterTypes()[i]));
				}

				// return proxy
				CyclicProxy proxy = cyclicProxies.get(key);
				if (proxy == null) {
					proxy = new CyclicProxy();
					cyclicProxies.put(key, proxy);
				}

				return introspector.newProxyInstance(proxy, params);

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
	
	private static class IntrospectorPredicate implements Predicate<Class> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean apply(Class obj) {
			return Introspector.of(obj).isAnnotationPresent(Component.class);
		}
		
	}
}
