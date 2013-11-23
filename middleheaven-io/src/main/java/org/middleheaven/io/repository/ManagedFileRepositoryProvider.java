package org.middleheaven.io.repository;

import java.net.URI;
import java.util.Map;



/**
 * 
 */
public interface ManagedFileRepositoryProvider  {


	/**
	 * Open a file repository on the given URI.
	 * 
	 * @param uri
	 * @return
	 * @throws RepositoryCreationException if it was not possible to open the repository. 
	 */
	public ManagedFileRepository newRepository(URI uri, Map<String, Object> params) throws RepositoryCreationException;
	
	/**
	 * The URI scheme this provider can handle.
	 * @return
	 */
	public String getScheme();
}
