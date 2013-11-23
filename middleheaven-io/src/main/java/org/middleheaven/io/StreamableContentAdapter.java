/**
 * 
 */
package org.middleheaven.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 */
public abstract class StreamableContentAdapter implements StreamableContent {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream() throws StreamNotReadableIOException,ManagedIOException {
		if (this.isReadable()){
			return resolveInputStream();
		}
		throw new StreamNotReadableIOException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream() throws StreamNotWritableIOException,
			ManagedIOException {
		if (this.isWritable()){
			return resolveOutputStream();
		}
		throw new StreamNotWritableIOException();
	}

	
	/**
	 * @return
	 */
	protected abstract InputStream resolveInputStream();
	protected abstract OutputStream resolveOutputStream();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getSize() throws ManagedIOException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContentTypeReadable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContentType() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContentTypeWritable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContentType(String contentType) throws ManagedIOException {
		throw new UnsupportedOperationException("Not implememented yet");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setSize(long size) throws ManagedIOException {
		//no-op
		return false;
	}

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
	public boolean isReadable() {
		return false;
	}
	
	

}
