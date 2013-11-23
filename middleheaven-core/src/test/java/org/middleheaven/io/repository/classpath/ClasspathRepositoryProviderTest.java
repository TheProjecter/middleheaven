/**
 * 
 */
package org.middleheaven.io.repository.classpath;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Test;
import org.middleheaven.core.annotations.Wire;
import org.middleheaven.io.filerepository.ClassPathRepositoryProvider;
import org.middleheaven.io.filerepository.FileRepositoryService;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.RepositoryCreationException;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


/**
 * 
 */
public class ClasspathRepositoryProviderTest extends MiddleHeavenTestCase{

	FileRepositoryService service;
	
	@Wire
	public void setFileService(FileRepositoryService service ){
		this.service  = service;
	}
	
	@Test
	public void testOpen() throws RepositoryCreationException, URISyntaxException{

		ClassPathRepositoryProvider provider = new ClassPathRepositoryProvider(service);

		
		ManagedFileRepository repo = provider.newRepository(new URI("classpath:org.middleheaven.io.repository.classpath.ClasspathRepositoryProviderTest"), Collections.<String,Object>emptyMap());
		
		
		ManagedFile file = repo.retrive(repo.getPath("test.txt"));
		
		assertTrue(file.exists());
	}
}
