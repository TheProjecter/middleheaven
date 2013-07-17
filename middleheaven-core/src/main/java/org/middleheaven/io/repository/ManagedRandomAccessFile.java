/**
 * 
 */
package org.middleheaven.io.repository;

import java.io.IOException;
import java.nio.channels.FileChannel;

import org.middleheaven.io.repository.machine.MachineFiles;

/**
 * 
 */
public class ManagedRandomAccessFile  implements java.io.DataOutput, java.io.DataInput, java.io.Closeable{

	
	private ManagedRandomAccessFileStrategy strategy;

	
	public ManagedRandomAccessFile (ManagedFile file, String mode){
		strategy = resolveStrategy(file,mode);
	}

	/**
	 * @return
	 */
	 public FileChannel getChannel() {
		 return strategy.getChannel();
	 }
	 
	/**
	 * @param file
	 * @param mode
	 * @return
	 */
	private ManagedRandomAccessFileStrategy resolveStrategy(ManagedFile file, String mode) {
		if (MachineFiles.isMachineFile(file) /*file is machine*/){
			
			if (!file.exists()){
				file = file.createFile();
			}
			return new RandomAccessFileStrategy(MachineFiles.randomAcessFile(file, mode));
		} else {
			throw new IllegalArgumentException("not supported yet");
		}
	}

	
	public int read(byte[] b, int i, int readLength) throws IOException {
		return strategy.read(b, i, readLength);
	}
	
	/**
	 * @param position2
	 */
	public void seek(long position) throws IOException{
		strategy.seek(position);
	}
	
	/**
	 * @param length
	 * @throws IOException 
	 */
	public void setLength(long length) throws IOException {
		strategy.setLength(length);
	}
	
	public long length() throws IOException{
		return strategy.length();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void close() throws IOException {
		strategy.close();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean readBoolean() throws IOException {
		return strategy.readBoolean();
	}

	/**
	 * {@inheritDoc}
	 */
	public byte readByte() throws IOException {
		return strategy.readByte();
	}

	/**
	 * {@inheritDoc}
	 */
	public char readChar() throws IOException {
		return strategy.readChar();
	}

	/**
	 * {@inheritDoc}
	 */
	public double readDouble() throws IOException {
		return strategy.readDouble();
	}

	/**
	 * {@inheritDoc}
	 */
	public float readFloat() throws IOException {
		return strategy.readFloat();
	}

	/**
	 * {@inheritDoc}
	 */
	public void readFully(byte[] b, int off, int len) throws IOException {
		strategy.readFully(b, off, len);
	}

	/**
	 * {@inheritDoc}
	 */
	public void readFully(byte[] b) throws IOException {
		strategy.readFully(b);
	}

	/**
	 * {@inheritDoc}
	 */
	public int readInt() throws IOException {
		return strategy.readInt();
	}

	/**
	 * {@inheritDoc}
	 */
	public String readLine() throws IOException {
		return strategy.readLine();
	}

	/**
	 * {@inheritDoc}
	 */
	public long readLong() throws IOException {
		return strategy.readLong();
	}

	/**
	 * {@inheritDoc}
	 */
	public short readShort() throws IOException {
		return strategy.readShort();
	}

	/**
	 * {@inheritDoc}
	 */
	public String readUTF() throws IOException {
		return strategy.readUTF();
	}

	/**
	 * {@inheritDoc}
	 */
	public int readUnsignedByte() throws IOException {
		return strategy.readUnsignedByte();
	}

	/**
	 * {@inheritDoc}
	 */
	public int readUnsignedShort() throws IOException {
		return strategy.readUnsignedShort();
	}

	/**
	 * {@inheritDoc}
	 */
	public int skipBytes(int n) throws IOException {
		return strategy.skipBytes(n);
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(byte[] b, int off, int len) throws IOException {
		strategy.write(b, off, len);
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(byte[] b) throws IOException {
		strategy.write(b);
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(int b) throws IOException {
		strategy.write(b);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeBoolean(boolean v) throws IOException {
		strategy.writeBoolean(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeByte(int v) throws IOException {
		strategy.writeByte(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeBytes(String s) throws IOException {
		strategy.writeBytes(s);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeChar(int v) throws IOException {
		strategy.writeChar(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeChars(String s) throws IOException {
		strategy.writeChars(s);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeDouble(double v) throws IOException {
		strategy.writeDouble(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeFloat(float v) throws IOException {
		strategy.writeFloat(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeInt(int v) throws IOException {
		strategy.writeInt(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeLong(long v) throws IOException {
		strategy.writeLong(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeShort(int v) throws IOException {
		strategy.writeShort(v);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeUTF(String s) throws IOException {
		strategy.writeUTF(s);
	}

	
	
	
}
