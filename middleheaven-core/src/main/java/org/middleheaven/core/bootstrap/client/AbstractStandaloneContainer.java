/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.ContainerFileSystem;
import org.middleheaven.core.bootstrap.StandardContainerFileSystem;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;

/**
 *
 */
public class AbstractStandaloneContainer implements BootstrapContainer {

	
	private StandardContainerFileSystem fileSystem;

	/**
	 * 
	 * Constructor.
	 * @param rootFolder the root folder where the container information can be stored.
	 */
	public AbstractStandaloneContainer(ManagedFile rootFolder) {

		this.fileSystem = new StandardContainerFileSystem(rootFolder);
	}
	
	@Override
	public void configurate(BootstrapContext context) {
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
    public String getContainerName() {
    	// TODO details, OS, version.. 
        return "standalone";
    }
    
	@Override
	public ContainerFileSystem getFileSystem() {
		return fileSystem;
	}






}
