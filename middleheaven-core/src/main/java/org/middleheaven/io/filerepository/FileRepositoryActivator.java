/**
 * 
 */
package org.middleheaven.io.filerepository;

import java.util.Collection;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.io.repository.machine.MachineFileSystemRepositoryProvider;

/**
 * 
 */
public class FileRepositoryActivator extends ServiceActivator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		//no-dependencies
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(FileRepositoryService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		
		MapFileRepositoryService service = new MapFileRepositoryService();
		
		service.registerProvider(new ClassPathRepositoryProvider(service));
		service.registerProvider(MachineFileSystemRepositoryProvider.getProvider());
		
		// TODO VFS
		
		serviceContext.register(FileRepositoryService.class, service);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(FileRepositoryService.class);
	}

}
