/**
 * 
 */
package org.middleheaven.io.repository.upload;

import java.io.OutputStream;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContentAdapter;

/**
 * 
 */
public abstract class AbstractRequestFileStreamableContent extends StreamableContentAdapter{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OutputStream resolveOutputStream() {
		throw new UnsupportedOperationException("Not implememented yet");
	}
	
	@Override
	public abstract long getSize() throws ManagedIOException;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContentTypeReadable() {
		return true;
	}

	@Override
	public abstract String getContentType();

	@Override
	public void setContentType(String contentType) {
		// not supported. fail silently
	}


}
