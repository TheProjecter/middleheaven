package org.middleheaven.io.repository.machine;

import java.net.URI;

import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.watch.WatchService;

/**
 * The file system access strategy
 */
interface MachineFileSystemAcessStrategy {


	/**
	 * @param uri
	 * @return
	 */
	public abstract  ManagedFileRepository openFileRepository(URI uri);
	
	public abstract WatchService getWatchService();


	
	
	
}
