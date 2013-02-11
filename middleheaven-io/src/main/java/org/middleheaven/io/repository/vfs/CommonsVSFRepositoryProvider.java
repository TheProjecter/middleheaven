package org.middleheaven.io.repository.vfs;

import java.net.URI;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.io.repository.RepositoryCreationException;

// TODO change to an activator
public class CommonsVSFRepositoryProvider implements ManagedFileRepositoryProvider {

	private VirtualFileWatchService virtualFileWatchService = new VirtualFileWatchService();
	private String scheme;

	public CommonsVSFRepositoryProvider (String scheme){
		this.scheme = scheme;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(URI uri ,Map<String, Object> params)
			throws RepositoryCreationException {
		try {
			FileObject file = VFS.getManager().resolveFile(uri.toString());
			
			return new VirtualFileSystemManagedFile(virtualFileWatchService, file);
		} catch (FileSystemException e) {
			throw new RepositoryCreationException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScheme() {
		return scheme; 	
	}
	
}
