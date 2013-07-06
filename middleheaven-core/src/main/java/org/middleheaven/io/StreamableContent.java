package org.middleheaven.io;

import java.io.InputStream;
import java.io.OutputStream;


public interface StreamableContent {

	
	public InputStream getInputStream() throws ManagedIOException;
	
	public OutputStream getOutputStream() throws ManagedIOException;
	
    /**
     * Returns the length of the content in bytes
     * @return
     * @throws ManagedIOException
     */
    public long getSize() throws ManagedIOException ;
    
    /**
     * Allow for establishing a start buffer size for the file contents.
     * This method is optional. Classes that do no support it must fail 
     * silently and return false.
     * 
     * @param size
     * @return {@code true} if the size was changed, {@code false} otherwise;
     * @throws ManagedIOException
     */
    public boolean setSize(long size) throws ManagedIOException ;
    
    
}
