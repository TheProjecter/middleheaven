/**
 * 
 */
package org.middleheaven.io.repository;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


/**
 * 
 */
public class ManagedFilePathTest  extends MiddleHeavenTestCase {


	private FileRepositoryService fileService;

	@Wire
	public void setFileRepositoryService (FileRepositoryService fileService){
		this.fileService = fileService;
	} 

	@Test
	public void testRelative(){

		ManagedFileRepository repository = fileService.newRepository(URI.create("file://"));
		
		ManagedFilePath path = new ArrayManagedFilePath(repository , "c:", "directoryA" , "directoryB");
		ManagedFilePath pathRelative = new ArrayManagedFilePath(repository , "c:", "directoryA" , "directoryB", "someFile.txt");
		ManagedFilePath pathRelativeSibling = new ArrayManagedFilePath(repository , "c:", "directoryA" , "directoryC", "someFile.txt");
		
		assertEquals(pathRelative, path.resolve("someFile.txt"));
		assertEquals(pathRelativeSibling, path.resolveSibling("directoryC/someFile.txt"));
		assertEquals(path, pathRelative.getParent());
	}
}