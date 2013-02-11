package org.middleheaven.logging;

import java.net.MalformedURLException;
import java.util.Collection;

import org.middleheaven.core.bootstrap.FileContext;
import org.middleheaven.core.bootstrap.FileContextService;
import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.io.repository.ManagedFile;

/**
 * Ativates log keeping. This activator will create a {@link ConfigurableLogListener}
 * and will register it with the environment {@link LoggingService}.
 */
public class LogKeepingActivator extends ServiceActivator {

	LoggingService loggingService;
	
	public LogKeepingActivator(){}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(LoggingService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		
	}
	

	@Override
	public void activate(ServiceContext serviceContext) {

		loggingService = new BroadcastLoggingService();
		
		final FileContext fileRepositoryService = serviceContext.getService(FileContextService.class).getFileContext();
		
		ManagedFile configFolder = fileRepositoryService.getEnvironmentConfigRepository();

		LoggingConfigurator configurator = new BasicConfigurator();
		LoggingConfiguration configuration = new LoggingConfiguration(null);

		if (configFolder != null && configFolder.exists() && configFolder.isReadable()){
			ManagedFile configXML = configFolder.retrive("log-config.xml");
			if (configXML!=null && configXML.exists() && configXML.isReadable()){
				try {
					configurator = new XMLLoggingConfigurator(configXML.getURI().toURL());
				} catch (MalformedURLException e) {
					throw new IllegalArgumentException(e);
				}
				ManagedFile logFolder = fileRepositoryService.getAppLogRepository();
				configuration = new LoggingConfiguration(logFolder);
			}
		} 
		//else,  don't bother to configure
	
		ConfigurableLogListener listener = new ConfigurableLogListener(configuration, configurator);

		this.loggingService.addLogListener(listener);
	}

	@Override
	public void inactivate(ServiceContext serviceContext) {
		loggingService = null;
	}



}
