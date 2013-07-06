/**
 * 
 */
package org.middleheaven.application;

import java.util.Collection;

import org.middleheaven.core.FileContextService;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.logging.LoggingService;

/**
 * Standard {@link ApplicationService} activator.  
 */
public class StandardApplicationServiceActivator extends ServiceActivator{

	
	public StandardApplicationServiceActivator(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(
			Collection<ServiceSpecification> specs) {
		specs.add( ServiceSpecification.forService(BootstrapService.class));
		specs.add( ServiceSpecification.forService(LoggingService.class));
		specs.add( ServiceSpecification.forService(FileContextService.class));
		
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(
			Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(ApplicationService.class));
	}

	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
	
		ModularApplicationService applicationService = new ModularApplicationService(serviceContext);
		
		serviceContext.register(ApplicationService.class, applicationService);
	
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
	
		ModularApplicationService applicationService = (ModularApplicationService) serviceContext.getService(ApplicationService.class);
		
		
		applicationService.unloadApplications();
	}

}
