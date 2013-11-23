/**
 * 
 */
package org.middleheaven.io.repository.empty;

import java.io.InputStream;
import java.io.OutputStream;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamNotReadableIOException;
import org.middleheaven.io.StreamNotWritableIOException;
import org.middleheaven.io.StreamableContent;

/**
 * 
 */
public class AllwaysEmpyMediaManagedFileContent implements StreamableContent {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream() throws ManagedIOException {
		throw new StreamNotReadableIOException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream() throws ManagedIOException {
		throw new StreamNotWritableIOException();
	}

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
	public boolean setSize(long size) throws ManagedIOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContentType() {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContentType(String contentType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadable() {
		return false;
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
	public boolean isWritable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContentTypeWritable() {
		return false;
	}

}
