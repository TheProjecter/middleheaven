/**
 * 
 */
package org.middleheaven.io;

import java.io.InputStream;

/**
 * 
 */
public interface ReadableStreamableContent {

	public abstract InputStream getInputStream() throws StreamNotReadableIOException, ManagedIOException;

	/**
	 * Returns the length of the content in bytes
	 * @return
	 * @throws ManagedIOException
	 */
	public abstract long getSize() throws ManagedIOException;

	
	public boolean isReadable();
	
	public boolean isContentTypeReadable();
	
	/**
	 * 
	 * @return the MIME type for the stream content
	 */
	public String getContentType();
	
}