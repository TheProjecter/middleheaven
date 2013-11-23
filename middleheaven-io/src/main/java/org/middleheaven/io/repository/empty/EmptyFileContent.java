package org.middleheaven.io.repository.empty;

import java.io.InputStream;
import java.io.OutputStream;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContent;

public class EmptyFileContent implements StreamableContent {

	private static final EmptyFileContent me = new EmptyFileContent();
	
	// not a singleton , just an otimization
	public static EmptyFileContent getInstance(){
		return me;
	}
	
	private EmptyFileContent(){
		
	}
	
	
	@Override
	public InputStream getInputStream() throws ManagedIOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream getOutputStream() throws ManagedIOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getSize() throws ManagedIOException {
		return 0;
	}

	@Override
	public boolean setSize(long size) throws ManagedIOException {
		// irrelevant. fail silently
		return false;
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
	public String getContentType() {
		throw new UnsupportedOperationException("Not implememented yet");
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContentType(String contentType) throws ManagedIOException {
		throw new UnsupportedOperationException("Not implememented yet");
	}

}
