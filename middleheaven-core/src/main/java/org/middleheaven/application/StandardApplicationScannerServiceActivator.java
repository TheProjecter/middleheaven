/**
 * 
 */
package org.middleheaven.application;

import java.util.Collection;

import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.io.filecontext.FileContextService;
import org.middleheaven.logging.LoggingService;

/**
 * 
 */
public class StandardApplicationScannerServiceActivator extends
		ServiceActivator {

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(
			Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(BootstrapService.class));
		specs.add(ServiceSpecification.forService(LoggingService.class));
		specs.add(ServiceSpecification.forService(FileContextService.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(
			Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(ApplicationScanningService.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		
		FileContextService fileContextService = serviceContext.getService(FileContextService.class);
		
		StandardApplicationScanningService standardApplicationScanningService = new StandardApplicationScanningService(
				serviceContext.getService(ApplicationService.class)		
		);

		// standard scanner
		standardApplicationScanningService.addModuleScanner(
					new LibraryJarModuleScanner(
							fileContextService.getFileContext().getAppLibraryRepository()
					)
		);
		   
		serviceContext.register(ApplicationScanningService.class, standardApplicationScanningService);
		
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		//no-op
	}

}
