package org.middleheaven.core.bootstrap;

import java.util.Collections;
import java.util.Map;

import javax.management.ServiceNotFoundException;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceContextUndefinedException;
import org.middleheaven.core.services.ServiceNotAvailableException;

public final class ServiceRegistry {

	private ServiceRegistry(){}
	
    static ServiceContext context;

    
    /**
     * Returns a service implementation compatible with <code>serviceClass</code>
     * @param <T> the serviceClass type
     * @param serviceClass the serviceClass contract
     * @return a service implementation compatible with <code>serviceClass</code> contract.
     * @throws ServiceNotFoundException if no implementation is found for the service
     */
    public static <T> T getService(Class<T> serviceClass){
    	return getService(serviceClass, Collections.<String,Object>emptyMap());
    }
    
    /**
     * Returns a service implementation compatible with <code>serviceClass</code>
     * and the specified service properties
     * @param <T> the serviceClass type
     * @param serviceClass the serviceClass contract
     * @param properties the specific properties for the service implementation
     * @return a service implementation compatible with <code>serviceClass</code> contract.
     * @throws ServiceNotFoundException if no implementation is found for the service or the properties
     */
    public static <T> T getService(Class<T> serviceClass, Map<String, Object> properties){
    	if (context == null){
    		throw new ServiceContextUndefinedException();
    	}
    	T service = context.getService(serviceClass, properties);
    	if (service==null){
    		throw new ServiceNotAvailableException(serviceClass.getName());
    	}
    	return service;
    }

    public static <T, I extends T> void register(Class<T> serviceClass,I implementation) {
		context.register(serviceClass, implementation, null);
	}
    
	public static <T, I extends T> void register(Class<T> serviceClass,I implementation, Map<String,Object> properties) {
		context.register(serviceClass, implementation, properties);
	}

	public static void unRegister(Object implementation) {
		context.unRegister(implementation);
	}
	
	public static void unRegister(Class<?> serviceType) {
		context.unRegister(serviceType);
	}

}
