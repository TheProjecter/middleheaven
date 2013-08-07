/**
 * 
 */
package org.middleheaven.io.repository.machine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ModificationTracableManagedFile;

/**
 * Utility class to provide access to recurrently used machine file system's {@link ManagedFile}. 
 */
public final class MachineFiles {

	private MachineFiles (){}


	public static ManagedFile getDefaultFolder(){
		return resolveFile(new File(System.getProperty("user.dir")).getAbsoluteFile());
	}

	/**
	 * Provides simplified access to the user's home folder.
	 * This method translates the path found at <code>System.getProperty("user.home")</code> using the  
	 * {@link MachineFileSystemRepositoryProvider}.
	 * 
	 * @return a {@link ManagedFile} that represents the user home folder.
	 */
	public static ManagedFile getUserWorkspaceFolder(){
		return resolveFile(new File(System.getProperty("user.home")));
	}

	/**
	 * Retrieves the {@link ManagedFile} that corresponds with the given {@link java.io.File}. 
	 * This method translates the path found at <code>file.toURI()</code> using the {@link MachineFileSystemRepositoryProvider}. 
	 * 
	 * @param file the file to use as basis.
	 * @return the correspondent {@link ManagedFile}.
	 * @see java.io.File#toURI()
	 */
	public static ManagedFile resolveFile(File file){

		if (!file.isAbsolute()){
			throw new IllegalArgumentException("Cannot resolve a relative file path");
		}
		ManagedFileRepository repo = MachineFileSystemRepositoryProvider.getProvider().newRepository(
				URI.create("file:/"), 
				Collections.<String, Object>emptyMap() 
				);

		return repo.retrive(repo.getPath(file.toURI().getPath()));
	}


	/**
	 * Ensures the managedFile is a machine file.
	 * @param managedFile
	 * @return
	 */
	public static ManagedFile ensureMachineFile(ManagedFile managedFile) throws IllegalArgumentException {
		try {
			URI uri = managedFile.getURI();
			MachineFileSystemRepositoryProvider.getProvider().systemFileStrategy.openFileRepository(uri);
			return managedFile;
		} catch (Exception e){
			throw new IllegalArgumentException("File " + managedFile.getURI() + " is not a machine file");
		}
	}

	public static ModificationTracableManagedFile ensureModificationTracable(ManagedFile managedFile) throws IllegalArgumentException {

		if (managedFile instanceof ModificationTracableManagedFile){
			return (ModificationTracableManagedFile) managedFile;
		} else {
			throw new IllegalArgumentException("File " + managedFile.getURI() + " is not a Modification Traceable file");
		}
	}

	/**
	 * @param file
	 * @return
	 */
	public static boolean isMachineFile(ManagedFile file) {
		try {
			ensureMachineFile(file);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}

	}


	/**
	 * @param file
	 * @param mode
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static RandomAccessFile randomAcessFile(ManagedFile file, String mode)  {
		try {
			return new RandomAccessFile(new File( file.getURI()), mode);
		} catch (FileNotFoundException e) {
			throw ManagedIOException.manage(e);
		}
	}
}
