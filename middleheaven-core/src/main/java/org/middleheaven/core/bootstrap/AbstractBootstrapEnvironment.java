/**
 * 
 */
package org.middleheaven.core.bootstrap;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceSpecification;


/**
 * 
 */
public abstract class AbstractBootstrapEnvironment implements BootstrapEnvironment{

	private Map<String, Service> services = new HashMap<String, Service>();
	
	
	public AbstractBootstrapEnvironment(){}
	

	protected void addService(Service service){
		services.put(service.getServiceSpecification().getServiceContractType().getName(), service);
	}
	
	/**
	 * @param context
	 * @param service
	 */
	protected void addService(BootstrapContext context, Service service) {
		context.registerService(service);
        addService(service);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Service provideService(ServiceSpecification spec) {
		
		Service service = services.get(spec.getServiceContractType().getName());
		
		if (service != null){
			return service;
		}
		
		for (Service s : services.values()){
			if (s.getServiceSpecification().matches(spec)){
				return s;
			}
		}
		
		service = resolverRequestedService(spec);
		
		if (service != null){
			return service;
		}
		
		throw new ServiceNotAvailableException(spec.getServiceContractType().getName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void posConfigurate(BootstrapContext context) {
	
	}
	
	
	protected abstract Service resolverRequestedService(ServiceSpecification spec);
}
