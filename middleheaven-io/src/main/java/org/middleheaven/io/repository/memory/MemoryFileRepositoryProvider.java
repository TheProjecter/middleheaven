/**
 * 
 */
package org.middleheaven.io.repository.memory;

import java.net.URI;
import java.util.Map;

import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.io.repository.RepositoryCreationException;

/**
 * 
 */
public class MemoryFileRepositoryProvider implements ManagedFileRepositoryProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(URI uri , Map<String, Object> params) throws RepositoryCreationException {
		if (!uri.getScheme().equalsIgnoreCase(this.getScheme())){
			throw new RepositoryCreationException("Unsupported Scheme " + uri.getScheme());
		}
		return new MemoryManagedFileRepository();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScheme() {
		return "memory";
	}

}
