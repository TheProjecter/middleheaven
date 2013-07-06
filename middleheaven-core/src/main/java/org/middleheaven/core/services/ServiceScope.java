package org.middleheaven.core.services;

import org.middleheaven.core.bootstrap.RegistryServiceContext;
import org.middleheaven.core.wiring.AbstractScopePool;
import org.middleheaven.core.wiring.ResolutionContext;
import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.WiringQuery;

/**
 * Scope for services.
 * Services can be added and removed from the system, so
 * the pool in inject a proxy that can handle the service lifecycle.
 * 
 */
public class ServiceScope extends AbstractScopePool {


	private RegistryServiceContext serviceRegistryContext;

	
	/**
	 * Constructor.
	 * @param serviceRegistryContext
	 */
	public ServiceScope(RegistryServiceContext serviceRegistryContext) {
		this.serviceRegistryContext = serviceRegistryContext;
	}

	@Override
	public Object getInScope(ResolutionContext context, WiringQuery query, Resolver resolver) {

		if (resolver == null){
			throw new IllegalArgumentException("Argument 'resolver' is required");
		}

		try{
			return  serviceRegistryContext.getService(query.getContract(), query.getParams());

		} catch (ServiceNotAvailableException e){
			
			try {
				Object object = resolver.resolve(context, query);
				
				if(object ==null){
					return null;
				}
				
				serviceRegistryContext.register(query.getContract(),object  , query.getParams());
				this.fireObjectAdded(object);
				
				return serviceRegistryContext.getService(query.getContract(), query.getParams());
			} catch (Exception e2){
				throw e;
			}
			
		}

	}

	@Override
	public void clear() {
		//no-op
	}

	@Override
	public void remove(Object object) {
		serviceRegistryContext.unRegister(object);
		this.fireObjectRemoved(object);
	}

}
