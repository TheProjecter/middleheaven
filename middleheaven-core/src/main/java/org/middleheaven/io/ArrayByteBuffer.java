package org.middleheaven.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

public final class ArrayByteBuffer implements ByteBuffer{

	private byte[] buffer = new byte[0];
	private final ReentrantLock lock = new ReentrantLock(); /// Redo
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream(){
		//no contention
		return new ByteArrayInputStream(buffer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream(){
		lock.lock();
		return new BufferByteArrayOutputStream();
	}
	
	public byte[] getBytes(){
		return buffer;
	}
	
	private class BufferByteArrayOutputStream extends ByteArrayOutputStream{

		public void close() throws IOException{
			super.close();
			buffer = this.toByteArray();
			lock.unlock();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getSize() {
		return this.buffer.length;
	}

	private boolean setSize(int size) {
		byte[] newStream = new byte[size];
		System.arraycopy(buffer, 0, newStream, 0, buffer.length);
		synchronized (lock){ 
			buffer = newStream;
		}
		return this.buffer.length == size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setSize(long size) throws ManagedIOException {
		int intSize = (int)size;
		
		if (intSize != size){
			throw new ManagedIOException("To large stream");
		}
		
		return setSize(intSize);
	}
}
