/**
 * 
 */
package org.middleheaven.core.bootstrap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceEvent;
import org.middleheaven.core.services.ServiceListener;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceProxy;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.util.Hash;
import org.middleheaven.util.Maybe;

/**
 * Simple implementation for {@link ServiceContext}.
 */
public final class RegistryServiceContext implements ServiceContext{

	final Set<ServiceListener> serviceListeners = new CopyOnWriteArraySet<ServiceListener>();
	final Map<String , Set<ServiceBinding>> registry = new HashMap<String , Set<ServiceBinding>>();

	final Map<ServiceKey, Object> proxies = new HashMap<ServiceKey, Object>();

	private static final RegistryServiceContext ME = new RegistryServiceContext();

	/**
	 * Returns an instance of the {@link RegistryServiceContext}. This instance
	 * is particular to each class loader.
	 * 
	 * @return the instance of the {@link RegistryServiceContext}.
	 */

//	public static RegistryServiceContext getInstance(){
//		if (ServiceRegistry.context == null){
//			ServiceRegistry.context = ME;
//		}
//		return ME;
//	}

	RegistryServiceContext(){

		/* should bing all to shared ?
		this.addServiceListener(new ServiceListener(){

			@Override
			public void onEvent(ServiceEvent event) {
				if (event.getType().equals(ServiceEventType.ADDED) && event.serviceClass.equals(WiringService.class)){
					getService(WiringService.class, null).getObjectPool().addConfiguration(new BindConfiguration(){

						@Override
						public void configure(Binder binder) {
							binder.bind(ServiceContext.class).in(Shared.class).toInstance(RegistryServiceContext.this);
						}

					});
				}
			}

		});
		 */
	}

	private void fireServiceTemporaryRemoved(ServiceBinding binding){
		ServiceEvent event = new ServiceEvent(
				ServiceEvent.ServiceEventType.TEMPORARY_REMOVED, 
				binding.serviceClass,
				binding.implementation,
				binding.properties
				);
		for (ServiceListener s : serviceListeners){
			s.onEvent(event);
		}
	}

	private void fireServiceAdded(ServiceBinding binding){
		ServiceEvent event = new ServiceEvent(ServiceEvent.ServiceEventType.ADDED,
				binding.serviceClass,
				binding.implementation,
				binding.properties
				);
		for (ServiceListener s : serviceListeners){
			s.onEvent(event);
		}
	}

	private void fireServiceRemoved(ServiceBinding binding){
		ServiceEvent event = new ServiceEvent(ServiceEvent.ServiceEventType.REMOVED, 		
				binding.serviceClass,
				binding.implementation,
				binding.properties
				);
		for (ServiceListener s : serviceListeners){
			s.onEvent(event);
		}
	}


	@Override
	public <T, I extends T> void register(final Class<T> serviceClass, final I implementation, Map<String, ? extends Object> properties) {

		internalRegister(serviceClass, implementation, properties);

	}

	private void internalRegister(final Class<?> serviceClass,
			final Object implementation, Map<String, ? extends Object> properties) {
		if (properties == null){
			properties = Collections.<String, Object>emptyMap();
		}

		// find current binding
		ServiceBinding binding = this.selectedServiceBinding(serviceClass, properties);

		// if its a perfect match ( same service class, same parameters)
		if(binding!=null && binding.match(properties) == 1){

			// perfect match found. This will be substituted
			this.fireServiceTemporaryRemoved(binding);

		} 

		// init binding set for the service
		ServiceBinding b = new ServiceBinding(properties,implementation,serviceClass);
		Set<ServiceBinding> list = registry.get(serviceClass.getName());
		if (list==null ){
			list = new CopyOnWriteArraySet<ServiceBinding>();
			registry.put(serviceClass.getName(), list);
		} 


		// create and registry a proxy for the service
		proxify(b);

		try{
			list.add(b); // add new binding
			fireServiceAdded(b);

		} catch (RuntimeException e){
			list.remove(b);
			throw e;
		}
	}

	/**
	 * Unregister all implementations and options for the given service.
	 * @param serviceClass the service class to unregister.
	 */
	public void unRegister(Class<?> serviceClass) {

		Set<ServiceBinding> list = registry.get(serviceClass.getName());
		if (list!=null){
			for (ServiceBinding binding : list){
				this.fireServiceRemoved(binding);
			}
			list.clear(); // remove all for the service
			registry.remove(serviceClass.getName());

		}
	}

	@Override
	public <T, I extends T> void unRegister(Class<T> serviceClass,I implementation, Map<String,? extends Object> properties) {
		ServiceBinding<T> registeredBinding = selectedServiceBinding(serviceClass, properties);

		if (registeredBinding!=null){
			// already exists. remove it
			Set<ServiceBinding> list = registry.get(serviceClass.getName());
			if (list!=null && list.remove(registeredBinding) ){
				for (ServiceBinding binding : list){
					this.fireServiceRemoved(binding);
				}
			}
		}

	}

	/**
	 * Unregisters all services with the given implementation
	 */
	@Override
	public void unRegister(Object implementation) {
		for (Iterator<Map.Entry<String,Set<ServiceBinding>>> it= registry.entrySet().iterator(); it.hasNext();){

			Entry<String, Set<ServiceBinding>> entry = it.next();
			for (ServiceBinding binding : entry.getValue()){
				if(binding.implementation.equals(implementation)){
					it.remove();
					this.fireServiceRemoved(binding);
					// do not break. can be more than one
				}
			}
		}
	}


	private <T> ServiceBinding<T> selectedServiceBinding(Class<T> serviceClass, Map<String, ? extends Object> properties){
		ServiceBinding selected = null;
		Set<ServiceBinding> list = registry.get(serviceClass.getName());
		if (list!=null && !list.isEmpty()){
			// no matching is necessary, return any
			if (properties==null || properties.isEmpty()){
				return list.iterator().next(); // random selection is ok
			}

			// rank and match (return best match)
			double rank = 0;

			for (ServiceBinding b : list){
				if (b.match(properties)>rank){
					selected = b;
				}
			}
		}
		return selected;
	}
	@Override
	public <T> T getService(Class<T> serviceClass, Map<String,? extends Object> properties) {
		ServiceBinding<T> selected = selectedServiceBinding(serviceClass, properties);

		if (selected != null){
			return proxify(selected);
		}

		throw new ServiceNotAvailableException(serviceClass.getName());
	}


	private <T> T proxify(ServiceBinding<T> binding){
		ServiceKey key = new ServiceKey(binding.serviceClass,binding.properties);
		T proxy = (T)proxies.get(key);

		if (proxy==null){
			ServiceProxy<T> handler = new ServiceProxy<T>(binding.serviceClass,binding.properties);


			handler.setImplementation((T) binding.implementation);


			this.addServiceListener(handler);

			proxy = Introspector.of(binding.serviceClass).newProxyInstance(handler);

			proxies.put(key,proxy);
		}

		return proxy;
	}

	private static class ServiceKey {

		final Map<String,Object> properties;
		private final int hashCode;
		private final  Class<?> contractClass;

		public ServiceKey(Class<?> contractClass , Map<String,Object> properties) {
			if (properties==null || properties.isEmpty()){
				this.properties = Collections.emptyMap();
			} else if (!(properties instanceof SortedMap)){
				this.properties = new HashMap<String,Object>(properties);
			} else {
				this.properties = properties;
			}
			this.hashCode = contractClass.getName().hashCode();
			this.contractClass = contractClass;
		}

		public boolean equals(Object other){
			return other instanceof ServiceKey && equalsOther((ServiceKey)other);
		}

		public int hashCode(){
			return this.hashCode;
		}

		private boolean equalsOther(ServiceKey other){
			return this.contractClass.getName().equals(other.contractClass.getName()) && CollectionUtils.equalContents(properties, other.properties);
		}

	}

	private void addServiceListener(ServiceListener s) {
		serviceListeners.add(s);
	}

	private static class ServiceBinding<C> {

		Map<String, Object> properties;
		Object implementation;
		Class<C> serviceClass;

		public <I extends C> ServiceBinding(Map<String,? extends Object> properties, I implementation, Class<C> serviceClass) {
			super();
			this.serviceClass = serviceClass;
			this.implementation = implementation;

			if (properties==null || properties.isEmpty()){
				this.properties = Collections.emptyMap();
			} else {
				this.properties = new HashMap<String,Object>(properties);
			}

		}



		public boolean equals(Object other){
			return other instanceof ServiceBinding && equalsOther((ServiceBinding)other);
		}

		public int hashCode(){
			return Hash.hash(properties).hash(serviceClass.getName()).hashCode();
		}

		private boolean equalsOther(ServiceBinding other){
			return this.serviceClass.getName().equals(other.serviceClass.getName()) && CollectionUtils.equalContents(properties, other.properties);
		}

		/**
		 * 0 = Does not match at all
		 * 1 = Match exactly
		 * @param properties
		 * @return
		 */
		public double match (Map<String, ? extends Object> properties){
			if (this.properties.size() < properties.size()){
				return 0d;
			}

			int count=0;
			for (Map.Entry<String,? extends Object> entry : properties.entrySet() ){
				if (entry.getValue()!=null && entry.getValue().equals(this.properties.get(entry.getKey()))){
					count++;
				}
			}

			return count * 1d / properties.size();
		}
	}

	/**
	 * 
	 */
	public void clear() {
		this.registry.clear();
		this.proxies.clear();
		this.serviceListeners.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T, I extends T> void register(Class<T> serviceClass,
			I implementation) {
		this.register(serviceClass, implementation, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T, I extends T> void unRegister(Class<T> serviceClass,
			I implementation) {
		this.unRegister(serviceClass, implementation, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getService(Class<T> serviceClass) {
		return this.getService(serviceClass, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Maybe<T> getPossibleUnAvailableService(Class<T> serviceClass) {
		try {
			return Maybe.of(this.getService(serviceClass, null));
		}catch (ServiceNotAvailableException e){
			return Maybe.absent();
		}	
	}


}