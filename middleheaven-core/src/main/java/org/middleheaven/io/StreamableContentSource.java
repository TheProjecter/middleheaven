/**
 * 
 */
package org.middleheaven.io;


/**
 * 
 */
public interface StreamableContentSource {

	
    /**
     * 
     * @return a handler for the file content
     */
    public StreamableContent getContent();
    
    /**
     * Copy this contents to another source's contents.
     * @param other
     * @throws ManagedIOException
     */
    public void copyTo(StreamableContentSource other) throws ManagedIOException;
}
