/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;


/**
 * The real container to complete the bootstrap cycle.
 */
public interface BootstrapEnvironment  {

    public String getName();
    
    public FileContext getFileContext();
    
    /**
     * Established and returns the {@link ManagedFileRepositoryProvider} to use with this container.
     *  
     * @return the {@link ManagedFileRepositoryProvider} to use with this container.
     */
    public ManagedFileRepositoryProvider getManagedFileRepositoryProvider();
    
    /**
     * Called by the Bootstrap to give the environment a change configurate it self before any thing else.
     * @param context the bootstrat context.
     */
    public void preConfigurate(BootstrapContext context);
    
    /**
     * Called by the Bootstrap to give the environment a change configurate it self after all other configrations where done.
     * @param context the bootstrat context.
     */
    public void posConfigurate(BootstrapContext context);
    
    public void start();
    public void stop();

//	/**
//	 * @return
//	 */
//	public ApplicationModulesResolver getApplicationModulesResolver();
//    
	
	public Service provideService(ServiceSpecification spec);

}
