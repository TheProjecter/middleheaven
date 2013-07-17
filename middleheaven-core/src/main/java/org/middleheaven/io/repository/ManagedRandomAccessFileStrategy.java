/**
 * 
 */
package org.middleheaven.io.repository;

import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 
 */
public interface ManagedRandomAccessFileStrategy extends  java.io.DataOutput, java.io.DataInput, java.io.Closeable{

		/**
		 * @return
		 */
		long length() throws IOException ;

		/**
		 * @param length
		 */
		void setLength(long length) throws IOException;

		/**
		 * @param position
		 */
		public void seek(long position) throws IOException;
		
		/**
		 * 
		 * @param b
		 * @param i
		 * @param readLength
		 * @return
		 * @throws IOException
		 */
		public int read(byte[] b, int i, int readLength) throws IOException;

		/**
		 * @return
		 */
		FileChannel getChannel();
}
