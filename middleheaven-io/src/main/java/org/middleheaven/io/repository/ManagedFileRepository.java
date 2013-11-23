/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository;

import java.io.Closeable;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.io.repository.watch.Watchable;

/**
 * A repository for {@link ManagedFile}s. 
 * 
 */
public interface ManagedFileRepository extends Closeable {

	/**
	 * 
	 * @return an {@link Iterable} for all the roots in this repository.
	 */
	public Iterable<ManagedFilePath> getRootPaths();
	
	/**
	 * Returns the {@link ManagedFileRepository} correspondent to the first  {@link ManagedFilePath} as given by {@link #getRootPaths()}
	 * @return
	 */
	public ManagedFile getFirstRoot();
	
	/**
	 * 
	 * @return <code>true</code> if the repository is open and can be used, <code>false</code> otherwise.
	 */
	public boolean isOpen();
	
    /**
     * 
     * @return logic value indicating if this repository supports reading capability
     */
    public  boolean isReadable();
    
    /**
     * 
     * @return logic value indicating if this repository supports writing capability
     */
    public  boolean isWriteable();

	/**
	 * logic value indicating if this repository supports watch semantics.
     * If it does , it must implement {@link Watchable}
	 * @return <code>true</code> if the registration of listeners and event trigger are supported.
	 */
	public boolean isWatchable();
	
	/**
	 * 
	 * @return
	 * @throws UnsupportedOperationException if this repository does not support the WatchService
	 */
	public WatchService getWatchService();
	
	
    /**
     * Determines if a file exists in the repository
     * @param path the file name. Example: config.properties, config.xml
     * @return
     * @throws FileStorageException
     */
    public  boolean exists(ManagedFilePath path) throws ManagedIOException;
    
    /**
     * Deletes the file from the repository
     * @param path the file name. Example: config.properties, config.xml
     * @return
     * @throws FileStorageException
     */
    public  boolean delete(ManagedFilePath path) throws ManagedIOException;

    /**
     * Retrieves a ManagedFile representing the file with the specified path.
     * The file might not exist.
     * 
     * @param path the path to the file.
     * @return 
     */
    public  ManagedFile retrive(ManagedFilePath path) throws ManagedIOException;

	/**
	 * @param string
	 * @return
	 */
	public ManagedFilePath getPath(String first, String ... more);

	/**
	 * Calculates the file size in bytes
	 * @param abstractManagedFile
	 * @return
	 */
	public long resolveFileSize(ManagedFile managedFile);


    
}
