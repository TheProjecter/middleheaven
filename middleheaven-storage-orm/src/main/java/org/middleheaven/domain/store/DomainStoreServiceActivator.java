package org.middleheaven.domain.store;

import java.util.Collection;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.core.wiring.WiringService;

public class DomainStoreServiceActivator extends ServiceActivator {

	private HashDomainStoreService storeService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add( ServiceSpecification.forService(WiringService.class));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add( ServiceSpecification.forService(DomainStoreService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		storeService = new HashDomainStoreService();
		
		serviceContext.register(DomainStoreService.class, storeService);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(DomainStoreService.class);
	}
}
