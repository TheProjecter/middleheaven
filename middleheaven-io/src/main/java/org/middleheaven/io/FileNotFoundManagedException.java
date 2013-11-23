/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io;

import java.io.File;


/**
 * Exception thrown when a requested file is not found in the repository.
 * 
 */
public class FileNotFoundManagedException extends ManagedIOException {

    
	private static final long serialVersionUID = 9129959484591489948L;
	
	protected String filename;
	
	/**
	 * 
	 * Constructor.
	 * @param file the file that was not found.
	 */
    public FileNotFoundManagedException(File file){
        this(file.getAbsolutePath());
    }
    
    /**
     * 
     * Constructor.
     * @param filename the name of the file that was not found.
     */
    public FileNotFoundManagedException(String filename){
        super(filename + " was not found.");
        this.filename = filename;
    }
    
    
    public String getFileName(){
        return filename;
    }
}
