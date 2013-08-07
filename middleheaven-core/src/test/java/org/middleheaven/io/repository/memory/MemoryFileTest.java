/**
 * 
 */
package org.middleheaven.io.repository.memory;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileType;

/**
 * 
 */
public class MemoryFileTest {

	@Test
	public void testFile() {
		
		ManagedFile root = MemoryFile.folder("/", new MemoryManagedFileRepository());
		
		ManagedFile file = root.retrive("file");
		
		file = file.createFile();
		
		assertEquals(ManagedFileType.FILE , file.getType());
	}
	
	@Test
	public void testFolder() {
		
		ManagedFile root = MemoryFile.folder("/", new MemoryManagedFileRepository());
		
		ManagedFile folder = root.retrive("folder");
		
		folder = folder.createFolder();
		
		assertEquals(ManagedFileType.FOLDER , folder.getType());
	}

}
