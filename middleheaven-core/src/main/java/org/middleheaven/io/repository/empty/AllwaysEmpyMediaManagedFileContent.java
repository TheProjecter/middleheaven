/**
 * 
 */
package org.middleheaven.io.repository.empty;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.MediaStreamableContent;

/**
 * 
 */
public class AllwaysEmpyMediaManagedFileContent implements
		MediaStreamableContent {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream() throws ManagedIOException {
		return new ByteArrayInputStream(new byte[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream() throws ManagedIOException {
		throw new UnsupportedOperationException();
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

}
