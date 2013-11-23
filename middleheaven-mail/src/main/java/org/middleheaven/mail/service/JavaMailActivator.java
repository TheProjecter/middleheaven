package org.middleheaven.mail.service;

import java.util.Collection;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.logging.Logger;
import org.middleheaven.mail.MailSendingService;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.util.Maybe;

public class JavaMailActivator extends ServiceActivator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(NameDirectoryService.class, true));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(MailSendingService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		
		Maybe<NameDirectoryService> maybeNameService = serviceContext.getPossibleUnAvailableService(NameDirectoryService.class);
		
		MailSendingService service;
		
		if(maybeNameService.isPresent()){
			Logger.onBookFor(this.getClass()).debug("Activating Mail Service from name directory");
			service = new NameDirectoryMailSessionSendingService(maybeNameService.get());
		} else {
			Logger.onBookFor(this.getClass()).debug("Activating Mail Service locally");
			service = new LocalMailSendingService();
		}
		
		serviceContext.register(MailSendingService.class, service);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(MailSendingService.class);
	}

}
