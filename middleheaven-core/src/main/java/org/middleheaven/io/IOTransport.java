/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Handles copying and moving bytes btween streams and/or files.
 * 
 */
public final class IOTransport {

	/**
	 * Copy interface.
	 */
	public interface IOCopy {

		/**
		 * Copy the original data to the given stream.
		 * @param outStream the stream to receive the data.
		 * @throws IOException if something goes wrong with the copy of the access to the stream.
		 */
		void to(OutputStream outStream) throws ManagedIOException;

		/**
		 * Copy the original data to the given file.
		 * @param outfile the file to receive the data.
		 * @throws IOException if something goes wrong with the copy of the access to the file.
		 */
		void to(File outfile) throws ManagedIOException;
		
		/**
		 * Copy the original data to the given {@link StreamableContent}.
		 * @param content the content to receive the data.
		 * @throws IOException if something goes wrong with the copy of the access to the stream.
		 */
		void to(StreamableContent content) throws ManagedIOException;
		
		/**
		 * Copy the original data to the given {@link StreamableContentSource}.
		 * @param content the content to receive the data.
		 * @throws IOException if something goes wrong with the copy of the access to the stream.
		 */
		void to(StreamableContentSource source) throws ManagedIOException;
	}

	/**
	 * Move interface.
	 */
	public interface IOMove {

		/**
		 * Moves the contents of the original file to the given file, and deletes the original file.
		 * @param outfile the file to receive the data.
		 * @throws IOException if something goes wrong with the copy of the access to the file.
		 */
		void to(File outfile) throws ManagedIOException;

		/**
		 * Moves the contents of the original file to a file with the same name as the original but in the given folder. Then, deletes the original file.
		 * @param outFolder the folder where to create the file to receive the data.
		 * @throws IOException if something goes wrong with the copy of the access to the file.
		 */
		void toFolder(File outFolder) throws ManagedIOException;
	}

	private IOTransport(){}


	/**
	 * Move the given file.
	 * @param file the file to move.
	 * @return a {@link IOMove} to complete the destination.
	 */
	public static IOMove move(final File file){
		return new IOMove(){

			@Override
			public void to(File outFile) throws ManagedIOException {
				move(file,outFile);
			}

			@Override
			public void toFolder(File outFolder) throws ManagedIOException {
				if (!outFolder.exists()) {
					if (!outFolder.mkdirs()){
						throw new ManagedIOException("Was not possible to create folder " + outFolder);
					}
				}

				File outFile = new File(outFolder, file.getName());

				move(file,outFile);
			}


		};
	}

	/**
	 * Copies the contents of a given {@link StreamableContent}.
	 * @param content the content to copy.
	 * @return a {@link IOCopy} to complete the destination.
	 */
	public static IOCopy copy(StreamableContent content){
		return copy(content.getInputStream());
	}
	
	/**
	 * Copies the contents of a given {@link StreamableContent}.
	 * @param content the content to copy.
	 * @return a {@link IOCopy} to complete the destination.
	 */
	public static IOCopy copy(StreamableContentSource source){
		return copy(source.getContent().getInputStream());
	}
	
	/**
	 * Copies the contents of a given stream.
	 * @param inStream the stream to copy.
	 * @return a {@link IOCopy} to complete the destination.
	 */
	public static IOCopy copy(final InputStream inStream){
		return new IOCopy(){

			@Override
			public void to(OutputStream outStream) throws ManagedIOException {
				copy(inStream, outStream);
			}

			@Override
			public void to(File outfile) throws ManagedIOException {
				try{
					copy(inStream, new FileOutputStream(outfile));
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}

			@Override
			public void to(StreamableContent content) throws ManagedIOException {
				copy(inStream, content.getOutputStream());
			}

			@Override
			public void to(StreamableContentSource source)
					throws ManagedIOException {
				copy(inStream, source.getContent().getOutputStream());
			}

		};
	}

	/**
	 * Copies the contents of a given byte array.
	 * @param bytes the byte array to copy.
	 * @return a {@link IOCopy} to complete the destination.
	 */
	public static IOCopy copy(final byte[] bytes){
		return new IOCopy(){

			@Override
			public void to(OutputStream outStream) throws ManagedIOException {
				copy(bytes, outStream);
			}

			@Override
			public void to(File outfile) throws ManagedIOException {
				try {
					copy(bytes, new FileOutputStream(outfile));
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}

			@Override
			public void to(StreamableContent content) throws ManagedIOException {
				copy(bytes, content.getOutputStream());
			}

			@Override
			public void to(StreamableContentSource source)
					throws ManagedIOException {
				copy(bytes, source.getContent().getOutputStream());
			}
		};
	}

	/**
	 * Copy the given file.
	 * @param file the file to copy.
	 * @return a {@link IOMove} to complete the destination.
	 */
	public static IOCopy copy(final File inFile){
		return new IOCopy(){

			@Override
			public void to(OutputStream outStream) throws ManagedIOException {
				try {
					copy(new FileInputStream(inFile), outStream);
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}

			@Override
			public void to(File outfile) throws ManagedIOException {
				copy(inFile, outfile);
			}

			@Override
			public void to(StreamableContent content) throws ManagedIOException {
				try {
					copy(new FileInputStream(inFile), content.getOutputStream());
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}

			@Override
			public void to(StreamableContentSource source)
					throws ManagedIOException {
				try {
					copy(new FileInputStream(inFile), source.getContent().getOutputStream());
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}
		};
	}

	/**
	 * Closes an I/O resource. Possible IOException is encapsulated in 
	 * an ManagedIOException
	 * @param closeable
	 * @throws ManagedIOException if an IOEXception is throwned by the closeable
	 */
	private static void close(Closeable closeable){
		try {
			closeable.close();
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}

	}

	private static void doStreamCopy(InputStream in,OutputStream out) throws ManagedIOException {
		try{
			byte[] buffer = new byte[1024 * 4]; //4 Kb
			int n = 0;
			while (-1 != (n = in.read(buffer))) {
				out.write(buffer, 0, n);
			}
			out.flush();

			out.close();
			in.close();
		}catch (IOException e){
			throw ManagedIOException.manage(e);
		}
	}

	private static void doCopyFile(FileInputStream in , FileOutputStream out)  {
		FileChannel inChannel = null, outChannel = null;

		try {

			inChannel = in.getChannel();
			outChannel = out.getChannel();

			inChannel.transferTo(0, inChannel.size(), outChannel);

		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		} finally {
			if (inChannel != null) {
				close(inChannel);
			}
			if (outChannel != null) {
				close(outChannel);
			}
		}

	}

	/**
	 * The copy does not closes the steams.
	 * @param data - the data to write to the output stream
	 * @param out out the stream to write to
	 * 
	 * @see IOTransport#close(Closeable)
	 */
	public static void copy(byte[] data, OutputStream out) {
		if(out == null){
			throw new IllegalArgumentException("Cannot copy to a non existent stream");
		}
		try {
			out.write(data);
		} catch (IOException e) {
			ManagedIOException.manage(e);
		}

	}

	/**
	 * The copy does not closes the steams.
	 * @param in the stream to read from
	 * @param out the stream to write to
	 * @throws IOException if something goes wrong
	 * @throws IllegalArgumentException if any argument is <code>null</code>
	 */
	private static void copy(InputStream in, OutputStream out) throws ManagedIOException {
		if (in==null || out ==null){
			throw new IllegalArgumentException("Cannot copy a non existent stream");
		}

		if (in instanceof FileInputStream && out instanceof FileOutputStream){
			doCopyFile((FileInputStream)in,(FileOutputStream)out);
		} else {
			doStreamCopy(in,out);
		}

	}

	/**
	 * Moves a file, i.e. copies the content to another file and deletes the original
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private static void move(File in,File out){
		if (!in.isFile()){
			throw new IllegalArgumentException("Can only move a file");
		}
		// if out is a directory, create a folder with the same name
		if (out.isDirectory()){
			out = new File (out.getAbsolutePath() + "/" + in.getName());
			out.mkdirs();
		}
		copy(in,out);
		in.delete();
	}

	/**
	 * Copies the data in one file to another existing file. 
	 * @param in the file to read 
	 * @param out the file to write
	 */
	private static void copy(File in, File out) {
		FileInputStream fis =null;
		FileOutputStream fos =null;

		try {
			fis  =new FileInputStream(in);
		} catch (FileNotFoundException e){
			throw new FileNotFoundManagedException(in);
		}

		try {
			fos = new FileOutputStream(out);
		} catch (FileNotFoundException e){
			throw new FileNotFoundManagedException(out);
		}

		doCopyFile(fis,fos);

	}


}
