package org.middleheaven.io.repository.machine;

import java.io.File;
import java.net.URI;

import org.middleheaven.io.UnsupportedSchemeException;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.watch.FileIOWatchService;
import org.middleheaven.io.repository.watch.WatchService;

/**
 * Standard access strategy uses {@link java.io.File}.
 */
class FileIOStrategy implements MachineFileSystemAcessStrategy {


	private FileIOWatchService service;

	/**
	 * 
	 * Constructor.
	 */
	FileIOStrategy (){

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository openFileRepository(URI uri) {
		if (!uri.getScheme().equals("file")){
			throw new UnsupportedSchemeException(uri.getScheme() + " is not supported");
		}
		
		if ( uri.getSchemeSpecificPart().equals("/") || isRoot(uri)) { 
			return new MachineIOSystemManagedFileRepository(this, null);
		} else {
			File file = new File(uri);
			if (file.isDirectory()) {
				return new MachineIOSystemManagedFileRepository(this, file);
			} else {
				return new MachineIOSystemManagedFileRepository(this, file.getParentFile());
			}
		}

		
	}

	/**
	 * @param uri
	 * @return
	 */
	private boolean isRoot(URI uri) {
		File file = new File(uri);
		
		for (File f : File.listRoots()){
			if (f.equals(file)){
				return true;
			}
		}
		
		return false;
		
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public synchronized WatchService getWatchService(){
		if (service == null) {
			service = new FileIOWatchService();
			service.start();
		}
		return service;
	}




}
