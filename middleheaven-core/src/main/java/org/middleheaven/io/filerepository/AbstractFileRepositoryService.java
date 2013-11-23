/**
 * 
 */
package org.middleheaven.io.filerepository;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.io.repository.RepositoryCreationException;



/**
 * 
 */
public abstract class AbstractFileRepositoryService implements FileRepositoryService{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(URI uri) throws RepositoryCreationException{
		return newRepository(uri, Collections.<String, Object>emptyMap());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(ManagedFilePath path)
			throws RepositoryCreationException {
		return newRepository(path, Collections.<String, Object>emptyMap());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(ManagedFilePath path, Map<String, Object> params) throws RepositoryCreationException {
//		FileRepositoryProvider provider = providers.get(uri.getScheme().toLowerCase());
//		
//		if (provider == null) {
//			throw new RepositoryCreationException("No provider found that could process shceme "  + uri.getScheme());
//		}
//		
//		return  provider.newRepository(uri, params);
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(URI uri, Map<String, Object> params) throws RepositoryCreationException {
		ManagedFileRepositoryProvider provider = resolveProviderByScheme(uri.getScheme().toLowerCase());
		
		if (provider == null) {
			throw new RepositoryCreationException("No provider found that could process shceme "  + uri.getScheme());
		}
		
		return  provider.newRepository(uri, params);
	}

	/**
	 * @param lowerCase
	 * @return
	 */
	protected abstract ManagedFileRepositoryProvider resolveProviderByScheme(String scheme);
	
}
