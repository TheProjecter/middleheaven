/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.bootstrap.AbstractBootstrapEnvironment;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.FileContext;
import org.middleheaven.core.bootstrap.StandardContainerFileSystem;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.io.repository.machine.MachineFileSystemRepositoryProvider;
import org.middleheaven.io.repository.machine.MachineFiles;

/**
 *
 */
public abstract class AbstractStandaloneBootstrapEnvironment extends AbstractBootstrapEnvironment {
	private StandardContainerFileSystem fileSystem;

	/**
	 * 
	 * Constructor.
	 * @param rootFolder the root folder where the container information can be stored.
	 */
	public AbstractStandaloneBootstrapEnvironment() {

		this.fileSystem = new StandardContainerFileSystem(MachineFiles.getDefaultFolder());
	}
	

	@Override
	public void preConfigurate(BootstrapContext context) {
		//no-op
	}
	
    @Override
	public void start() {
    	//no-op
    }

	@Override
	public void stop() {
		//no-op
	}
    
	@Override
	public FileContext getFileContext() {
		return fileSystem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepositoryProvider getManagedFileRepositoryProvider() {
		return MachineFileSystemRepositoryProvider.getProvider();
	}






}
