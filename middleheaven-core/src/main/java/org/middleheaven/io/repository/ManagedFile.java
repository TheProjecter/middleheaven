/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository;


import java.net.URI;

import org.middleheaven.collections.Enumerable;
import org.middleheaven.collections.TreeEnumerable;
import org.middleheaven.io.StreamableContentSource;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.watch.Watchable;


/**
 * A common abstract for all types of files : disk files, url addresses, uploaded files, email attachments, etc ...
 */
public interface ManagedFile extends Watchable , StreamableContentSource {

	/**
	 * the {@link ManagedFileRepository} where this file belongs.
	 * @return
	 */
	public ManagedFileRepository getRepository();
	
	public TreeEnumerable<ManagedFile> asTreeEnumerable();
	
	public Enumerable<ManagedFile> children();
	
	
	/**
	 * This file's path
	 * @return
	 */
	public ManagedFilePath getPath(); 

	/**
	 * determines if the base name of this file can be changed to a new one.
	 * It can not be changed if a file with that name already exists.
	 * Some implementations may not support the rename operation 
	 * @param newBaseName
	 * @return
	 */
	public boolean canRenameTo(String newBaseName);
	
	
	/**
	 * Change the base name to a new value
	 * @param name
	 * @throws ManagedIOException
	 */
	public void renameTo(String newBaseName) throws ManagedIOException;
	
	/**
	 * For FILE managed files it returns the same as <code>getContent().getSize</code>
	 * for FOLDER managed files it returns the sum of inner FILE and FOLDER getSize()
	 * 
	 * @return the byte length of the managed file.   
	 * @throws ManagedIOException
	 */
	public long getSize() throws ManagedIOException;
	
	/**
	 * Test if the another file is contained in this file.
	 * the file is contained if <code>this</code> file is the parent of <code>other</code>
	 * @param other
	 * @return
	 */
	public boolean contains(ManagedFile other);
	
	/**
	 *  Determines if this file exists in the repository.
	 */
	public boolean exists();
	
	/**
	 * 
	 * @return <code>true</code> if this implements <code>Watchable</code> and a <code>FileChangeListener</code> can be attached to this in order to listen to
	 * changes in the files container in this file.<code>false</code> otherwise.
	 */
	public boolean isWatchable();
	
	/**
	 * The parent {@link ManagedFile}
	 * @return the parent {@link ManagedFile}, or <code>null</code> if this a root folder.
	 */
	public ManagedFile getParent();
	
	/**
	 * The managed file type.
	 * @return the managed file type.
	 */
	public ManagedFileType getType();
	
	/**
	 * The file URL.
	 * @return the file URL.
	 */
	public URI getURI();
	
	 /**
     * Retrieves a ManagedFile representing the file with the specified name who's parent is {@code  this}.
     * 
     * @param path the file's name (name and extension).
     * @return the found file or null if this does not represent a folder.
     * 
     */
    public abstract ManagedFile retrive(String path) throws ManagedIOException;
    
	 /**
     * Retrieves a ManagedFile representing the file with the specified name who's parent is {@code  this}
     * @param path the file's name (name and extension).
     * @return the found file.
     * 
     */
    public abstract ManagedFile retrive(ManagedFilePath path) throws ManagedIOException;
    
	/**
	 * If {@code this} is of type {@link ManagedFileType#VIRTUAL} create and return a {@link ManagedFile} file on the URL pointed by {@code this}.
	 * The new file substitutes {@code this} on its parent.
	 * If {@code this} is not of type {@link ManagedFileType#VIRTUAL} and {@code this} is of type {@link ManagedFileType#FILE} {@ this } is returned.
	 * Else an {@link UnsupportedOperationException} is thrown;
	 * @return the created file
	 * @throws UnsupportedOperationException if {@code this} is not of VIRTUAL or FILE type, or this operation is not implemented on the target class.
	 */
	public ManagedFile createFile() throws UnsupportedOperationException;
    
	/**
	 * If {@code this} is of type {@link ManagedFileType#VIRTUAL} create and return a {@ ManagedFile} folder on the URL pointed by {@code this}.
	 * The new file substitutes {@code this} on its parent.
	 * If {@code this} is not of type {@link ManagedFileType#VIRTUAL} and {@code this} is of type {@link ManagedFileType#FOLDER} {@ this } is returned.
	 * Else an {@link UnsupportedOperationException} is thrown;
	 * @return the created folder
	 * @throws UnsupportedOperationException if {@code this} is not of VIRTUAL or FOLDER type, or this operation is not implemented on the target class.
	 */
	public ManagedFile createFolder() throws UnsupportedOperationException;

	
    /**
     * indicates if getInputStream() can be invoked
     * @return <code>true</code> if its possible to read from this file.
     */
    public boolean isReadable();
    
    /**
     * indicates if getOutputStream() can be invoked
     * @return <code>true</code> if its possible to write to this file.
     */
    public boolean isWriteable();

    /**
     * Copies the content of this file to another file
     * @param other the target file.
     * @throws ManagedIOException if something goes wrong.
     */
    public void copyTo(ManagedFile other) throws ManagedIOException;

    /**
     * Delete this file.
     * @return <code>true</code> if the file was deleted.
     */
	public boolean delete();


	/**
	 * Deletes all children files and folders, and all children in those folders.
	 */
	public void deleteTree();


    
}
