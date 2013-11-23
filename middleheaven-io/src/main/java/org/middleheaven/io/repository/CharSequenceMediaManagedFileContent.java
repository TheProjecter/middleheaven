package org.middleheaven.io.repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.middleheaven.io.ManagedIOException;

/**
 * A read-only {@link MediaStreamableContent} adapter to a CharSequence. 
 */
public class CharSequenceMediaManagedFileContent extends StreamBasedMediaManagedFileContent implements Serializable {


	private static final long serialVersionUID = 5541977209287934311L;
	
	private CharSequence body;

	public CharSequenceMediaManagedFileContent(CharSequence body, String contentType){
		this.body = body;
		this.setContentType(contentType);
	}

	public String getText(){
		return body.toString();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream() throws ManagedIOException {
		return new ByteArrayInputStream(body.toString().getBytes());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream() throws ManagedIOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public long getSize() throws ManagedIOException {
		return body.toString().getBytes().length;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean setSize(long size) throws ManagedIOException {
		throw new UnsupportedOperationException();
	}

}

