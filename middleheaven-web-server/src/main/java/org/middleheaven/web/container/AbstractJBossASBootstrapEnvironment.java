/*
 * Created on 2006/09/02
 *
 */
package org.middleheaven.web.container;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceBuilder;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.machine.MachineFiles;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.jndi.JNDINamingDirectoryActivator;

/**
 * 
 */
public abstract class AbstractJBossASBootstrapEnvironment extends StandardSevletBootstrapEnvironment {

    public AbstractJBossASBootstrapEnvironment(ServletContext context){
       super(context);
    }

    @Override
    public String getName() {
        return "jboss";
    }

    
    @Override
    public void preConfigurate(BootstrapContext context) {
 
        // Set JNDI default parameters
        System.setProperty("java.naming.factory.initial",  "org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url", "localhost:1099");
        

        this.addService(context, ServiceBuilder
						.forContract(NameDirectoryService.class)
						.activatedBy(new JNDINamingDirectoryActivator())
						.newInstance());
    }
    


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Service resolverRequestedService(ServiceSpecification spec) {
		return null;
	}


    public ManagedFile getEnvironmentConfigRepository() {
        return getEnvironmentDeployRepository();
    }

    public ManagedFile getEnvironmentDeployRepository() {
        return MachineFiles.resolveFile(System.getProperty("jboss.server.home.dir")).retrive("deploy");
    }

    public ManagedFile getAppConfigRepository() {
        return MachineFiles.resolveFile(System.getProperty("jboss.server.home.dir")).retrive("conf");
    }

    public ManagedFile getAppDataRepository() {
        return MachineFiles.resolveFile(System.getProperty("jboss.server.home.dir")).retrive("app-data");
    }

    public ManagedFile getAppLogRepository() {
        return MachineFiles.resolveFile(System.getProperty("jboss.server.home.dir")).retrive("log");
    }

    public ManagedFile getAppClasspathRepository() {
        return null; // TODO usar ClasspathRepository
    }
    
}
