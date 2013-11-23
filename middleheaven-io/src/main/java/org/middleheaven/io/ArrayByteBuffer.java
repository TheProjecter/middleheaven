package org.middleheaven.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A {@link ByteBuffer} implemented with an array
 */
public final class ArrayByteBuffer implements ByteBuffer{

	private final ReentrantReadWriteLock  lock = new ReentrantReadWriteLock(); 

	private byte[] buffer;


	public ArrayByteBuffer (){
		buffer = new byte[0];
	}

	public ArrayByteBuffer (int inicialCapacity){
		buffer = new byte[inicialCapacity];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream(){
		try {
			lock.readLock().lock(); // wait for possible writing operations
			return new ByteArrayInputStream(buffer);
		} finally {
			lock.readLock().unlock(); // free for further writes
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream(){
		// the client code will write to the stream inner buffer
		return new BufferByteArrayOutputStream();
	}

	private class BufferByteArrayOutputStream extends ByteArrayOutputStream{

		public void close() throws IOException{
			super.close(); // dump the inner buffer to the array
			try {
				lock.writeLock().lock(); // stop reads and other writes
				buffer = this.toByteArray(); // change the data
			} finally {
				lock.writeLock().unlock(); // allows for reads and other writes
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getSize() {
		try {
			lock.readLock().lock(); // wait for possible writing operations
			return this.buffer.length;
		} finally {
			lock.readLock().unlock(); // free for further writes
		}
	}

	private boolean setSize(int size) {
		try {
			lock.writeLock().lock();
			byte[] newStream = new byte[size];
			System.arraycopy(buffer, 0, newStream, 0, buffer.length);
			buffer = newStream;
			return this.buffer.length == size;
		} finally {
			lock.writeLock().unlock();
		}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadable() {
		return true;
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
		throw new ManagedIOException("Content type is not readable");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return true;
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
		throw new ManagedIOException("Content type is not writable");
	}
}
