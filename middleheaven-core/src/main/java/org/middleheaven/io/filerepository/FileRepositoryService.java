/**
 * 
 */
package org.middleheaven.io.filerepository;

import java.net.URI;
import java.util.Map;

import org.middleheaven.core.annotations.Service;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.io.repository.RepositoryCreationException;

/**
 * 
 */
@Service
public interface FileRepositoryService {

	
	public void registerProvider(ManagedFileRepositoryProvider provider);
	
	public void unRegisterProvider(ManagedFileRepositoryProvider provider);
	
	public ManagedFileRepository newRepository(URI uri) throws RepositoryCreationException;
	
	public ManagedFileRepository newRepository(URI uri, Map<String, Object> params) throws RepositoryCreationException;
	
	public ManagedFileRepository newRepository(ManagedFilePath path) throws RepositoryCreationException;
	
	public ManagedFileRepository newRepository(ManagedFilePath path, Map<String, Object> params) throws RepositoryCreationException;


}
