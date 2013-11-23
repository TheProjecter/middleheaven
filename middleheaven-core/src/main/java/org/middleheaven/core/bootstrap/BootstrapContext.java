package org.middleheaven.core.bootstrap;

import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceProvider;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.core.wiring.ProfilesBag;
import org.middleheaven.core.wiring.PropertyManagers;
import org.middleheaven.io.filecontext.FileContextService;
import org.middleheaven.logging.LoggingService;

/**
 * Context of information used during bootstrap.
 */
public interface BootstrapContext {

	
	public BootstrapContext requireService(ServiceSpecification spec) throws ServiceNotAvailableException;
	public BootstrapContext requireService(ServiceSpecification spec, ServiceProvider provider);
	
	/**
	 * Registers a new service to be bootstraped.
	 * @param service the service to be bootstraped
	 * @return {@code this} object.
	 */
	public BootstrapContext registerService(Service service);
	
	/**
	 * Unregisters a service from the bootstrap process.
	 * @param service the service to be bootstraped
	 * @return {@code this} object.
	 */
	public BootstrapContext removeService(Service service);
	
	/**
	 * Verifies if a given service is already registered.
	 * @param service the service to check registration.
	 * @return <code>true</code> if the service is alredy registered, <code>false</code> otherwise.
	 */
	public boolean isRegistered(Service service);
	
	/**
	 * The name of the environment.
	 * @return The name of the environment.
	 */
	public String getName();

	/**
	 * Active profiles {@link ProfilesBag}.
	 * @return  Active profiles {@link ProfilesBag}.
	 */
	public ProfilesBag getActiveProfiles();
	

	/**
	 * A {@link PropertyManagers} that provide acess to system and environment properties.
	 * @return {@link PropertyManagers} that provide acess to system and environment properties.
	 */
	public PropertyManagers getPropertyManagers();

	/**
	 * @return
	 */
	public LoggingService getLoggingService();

	/**
	 * @return
	 */
	public FileContextService getFileContextService();
	
}
