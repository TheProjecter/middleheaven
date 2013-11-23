/**
 * 
 */
package org.middleheaven.io.repository;

import java.io.File;
import java.net.URI;

import org.junit.Test;
import org.middleheaven.io.repository.machine.MachineFileSystemRepositoryProvider;


/**
 * 
 */
public class ManagedFilePathTest   {


//	private FileRepositoryService fileService;
//
//	@Wire
//	public void setFileRepositoryService (FileRepositoryService fileService){
//		this.fileService = fileService;
//	} 
//
//	@Test
//	public void testRelative(){
//
//		ManagedFileRepository repository = fileService.newRepository(URI.create("file://"));
//		
//		ManagedFilePath path = new ArrayManagedFilePath(repository , "c:", "directoryA" , "directoryB");
//		ManagedFilePath pathRelative = new ArrayManagedFilePath(repository , "c:", "directoryA" , "directoryB", "someFile.txt");
//		ManagedFilePath pathRelativeSibling = new ArrayManagedFilePath(repository , "c:", "directoryA" , "directoryC", "someFile.txt");
//		
//		assertEquals(pathRelative, path.resolve("someFile.txt"));
//		assertEquals(pathRelativeSibling, path.resolveSibling("directoryC/someFile.txt"));
//		assertEquals(path, pathRelative.getParent());
//	}
	@Test
	public void testLinux(){
		String root = "/home/javabuil/appservers/apache-tomcat-6x/webapps/javabuilding";
		
		MachineFileSystemRepositoryProvider provider = MachineFileSystemRepositoryProvider.getProvider();
		
		ManagedFileRepository repo = provider.newRepository(
				URI.create("file://" + root.replace(File.separatorChar, '/')),
				null
		);
	}
	
	@Test
	public void testWindows(){
		String root = "/c:/home/javabuil/appservers/apache-tomcat-6x/webapps/javabuilding";
		
		MachineFileSystemRepositoryProvider provider = MachineFileSystemRepositoryProvider.getProvider();
		
		ManagedFileRepository repo = provider.newRepository(
				URI.create("file://" + root.replace(File.separatorChar, '/')),
				null
		);
	}
}
