/**
 * 
 */
package org.middleheaven.io.repository.memory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileType;

/**
 * 
 */
public class MemoryFileTest {

	@Test
	public void testFile() {
		
		ManagedFile root = new MemoryManagedFileRepository().getFirstRoot();
		
		ManagedFile file = root.retrive("file");
		
		file = file.createFile();
		
		assertEquals(ManagedFileType.FILE , file.getType());
	}
	
	@Test
	public void testFolder() {
		
		ManagedFile root = new MemoryManagedFileRepository().getFirstRoot();
		
		ManagedFile folder = root.retrive("folder");
		
		folder = folder.createFolder();
		
		assertEquals(ManagedFileType.FOLDER , folder.getType());
	}

}
