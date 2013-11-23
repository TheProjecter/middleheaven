/**
 * 
 */
package org.middleheaven.io.repository;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;



/**
 * Implementation of {@link ManagedRandomAccessFileStrategy} has a {@link RandomAccessFile} decorator.
 */
public class RandomAccessFileStrategy implements
		ManagedRandomAccessFileStrategy {

	
	RandomAccessFile file;
	
	/**
	 * 
	 * Constructor.
	 * @param file the underlying {@link RandomAccessFile}
	 */
	public RandomAccessFileStrategy (RandomAccessFile file){
		this.file = file;
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws IOException {
		file.close();
	}


	/**
	 * {@inheritDoc}
	 */
	public final FileChannel getChannel() {
		return file.getChannel();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public long getFilePointer() throws IOException {
		return file.getFilePointer();
	}

	
	public boolean equals(Object other){
		return (other instanceof RandomAccessFileStrategy) && equalsOther(( RandomAccessFileStrategy) other);
	}
	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(RandomAccessFileStrategy other) {
		return file.equals(other.file);
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return file.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	public long length() throws IOException {
		return file.length();
	}

	/**
	 * {@inheritDoc}
	 */
	public int read() throws IOException {
		return file.read();
	}

	/**
	 * {@inheritDoc}
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		return file.read(b, off, len);
	}

	/**
	 * {@inheritDoc}
	 */
	public int read(byte[] b) throws IOException {
		return file.read(b);
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean readBoolean() throws IOException {
		return file.readBoolean();
	}

	/**
	 * {@inheritDoc}
	 */
	public final byte readByte() throws IOException {
		return file.readByte();
	}

	/**
	 * {@inheritDoc}
	 */
	public final char readChar() throws IOException {
		return file.readChar();
	}

	/**
	 * {@inheritDoc}
	 */
	public final double readDouble() throws IOException {
		return file.readDouble();
	}

	/**
	 * {@inheritDoc}
	 */
	public final float readFloat() throws IOException {
		return file.readFloat();
	}

	/**
	 * {@inheritDoc}
	 */
	public final void readFully(byte[] b, int off, int len) throws IOException {
		file.readFully(b, off, len);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void readFully(byte[] b) throws IOException {
		file.readFully(b);
	}

	/**
	 * {@inheritDoc}
	 */
	public final int readInt() throws IOException {
		return file.readInt();
	}

	/**
	 * {@inheritDoc}
	 */
	public final String readLine() throws IOException {
		return file.readLine();
	}

	/**
	 * {@inheritDoc}
	 */
	public final long readLong() throws IOException {
		return file.readLong();
	}

	/**
	 * {@inheritDoc}
	 */
	public final short readShort() throws IOException {
		return file.readShort();
	}

	/**
	 * {@inheritDoc}
	 */
	public final String readUTF() throws IOException {
		return file.readUTF();
	}

	/**
	 * {@inheritDoc}
	 */
	public final int readUnsignedByte() throws IOException {
		return file.readUnsignedByte();
	}

	/**
	 * {@inheritDoc}
	 */
	public final int readUnsignedShort() throws IOException {
		return file.readUnsignedShort();
	}

	/**
	 * {@inheritDoc}
	 */
	public void seek(long pos) throws IOException {
		file.seek(pos);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLength(long newLength) throws IOException {
		file.setLength(newLength);
	}

	/**
	 * {@inheritDoc}
	 */
	public int skipBytes(int n) throws IOException {
		return file.skipBytes(n);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return file.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(byte[] b, int off, int len) throws IOException {
		file.write(b, off, len);
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(byte[] b) throws IOException {
		file.write(b);
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(int b) throws IOException {
		file.write(b);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeBoolean(boolean v) throws IOException {
		file.writeBoolean(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeByte(int v) throws IOException {
		file.writeByte(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeBytes(String s) throws IOException {
		file.writeBytes(s);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeChar(int v) throws IOException {
		file.writeChar(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeChars(String s) throws IOException {
		file.writeChars(s);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeDouble(double v) throws IOException {
		file.writeDouble(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeFloat(float v) throws IOException {
		file.writeFloat(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeInt(int v) throws IOException {
		file.writeInt(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeLong(long v) throws IOException {
		file.writeLong(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeShort(int v) throws IOException {
		file.writeShort(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void writeUTF(String str) throws IOException {
		file.writeUTF(str);
	}
	
	
}
