/**
 * 
 */
package org.middleheaven.io;

import java.io.OutputStream;

/**
 * 
 */
public interface WritableStreamableContent {

	public abstract OutputStream getOutputStream() throws StreamNotWritableIOException, ManagedIOException;

	/**
	 * Allow for establishing a start buffer size for the file contents.
	 * This method is optional. Classes that do no support it must fail 
	 * silently and return false.
	 * 
	 * @param size
	 * @return {@code true} if the size was changed, {@code false} otherwise;
	 * @throws ManagedIOException
	 */
	public abstract boolean setSize(long size) throws ManagedIOException;

	public boolean isWritable();
	
	public boolean isContentTypeWritable();

	/**
	 * Set the MIME type for this file contents.
	 * This operation is optional. Classes that do not support it must fail silently
	 * 
	 * @param contentType the MIME type for this file contents
	 */
	public void setContentType(String contentType) throws ManagedIOException;
}