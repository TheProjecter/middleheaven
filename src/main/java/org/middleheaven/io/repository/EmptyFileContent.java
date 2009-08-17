package org.middleheaven.io.repository;

import java.io.InputStream;
import java.io.OutputStream;

import org.middleheaven.io.ManagedIOException;

public class EmptyFileContent implements ManagedFileContent {

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
		// not supported. fail silently
		return false;
	}

}