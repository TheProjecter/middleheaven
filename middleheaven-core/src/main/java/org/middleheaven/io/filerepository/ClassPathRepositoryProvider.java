/**
 * 
 */
package org.middleheaven.io.filerepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.io.repository.RepositoryCreationException;

/**
 * This {@link ManagedFileRepositoryProvider} uses the class provided in the URI and uses the {@link Class#getResource(String)} to find
 * the base URL for the {@link ManagedFileRepository}. Then it uses the  {@link FileRepositoryService} to find the final {@link ManagedFileRepository}.
 * 
 * This {@link ManagedFileRepositoryProvider} understands URI in the form <code>classpath:org.company.packageA.ClassName</code> 
 * 
 */
public class ClassPathRepositoryProvider implements ManagedFileRepositoryProvider {

	
	private FileRepositoryService fileService;

	public ClassPathRepositoryProvider (FileRepositoryService  fileRepositoryService){
		this.fileService =fileRepositoryService;
	}
	
	/**
	 * provides repositories in the form 
	 *  classpath:org.company.packageA.ClassName:/
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(URI uri,  Map<String, Object> params) throws RepositoryCreationException {
	
		if (!uri.getScheme().equalsIgnoreCase(this.getScheme())){
			throw new RepositoryCreationException("Unsupported Scheme " + uri.getScheme());
		}
		try {
			// 1- determine from url the base classe  . if no one exists, use this class 
			String path  = uri.getSchemeSpecificPart();
			
			Class<?> type = Class.forName(path);
	
			// 2 - use class to resolve the file location
			URL url =  type.getResource(".");
			
			if ( url == null){
				throw new RepositoryCreationException("Resource does not exist (" + uri + ")");
			}
			
			// 3 - delegate to fileService to create the real repository based on the URI that corresponds with the url
			return fileService.newRepository(url.toURI());
		
		} catch (ClassNotFoundException e) {
			throw new RepositoryCreationException(e);
		} catch (URISyntaxException e) {
			throw new RepositoryCreationException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScheme() {
		return "classpath";
	}

}
