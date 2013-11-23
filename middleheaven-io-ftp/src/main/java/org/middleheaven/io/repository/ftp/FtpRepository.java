/**
 * 
 */
package org.middleheaven.io.repository.ftp;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.middleheaven.io.ArrayByteBuffer;
import org.middleheaven.io.AutoInputStreamCopy;
import org.middleheaven.io.AutoOutputStreamCopy;
import org.middleheaven.io.IOTransport;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.StreamableContentAdapter;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.AbstractManagedRepository;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.util.Joiner;

/**
 * 
 */
public class FtpRepository extends AbstractManagedRepository{

	private FTPClient ftp = new FTPClient();
	private FtpCredentials ftpCredentials;

	public FtpRepository(FtpCredentials ftpCredentials)
	{
		this.ftpCredentials = ftpCredentials;

	}

	@Override
	public void close() throws IOException {
		ftp.logout();
	}

	/**
	 * 
	 */
	private FTPClient connect() {
		try {

			if (!ftp.isConnected()){
				ftp.connect(ftpCredentials.getFtpAddress());
				ftp.login( ftpCredentials.getUsername(), ftpCredentials.getPassword());
			}

			return ftp;
		} catch (IOException e) {
			throw  ManagedIOException.manage(e);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<ManagedFilePath> getRootPaths() {
		return Collections.singleton((ManagedFilePath)new ArrayManagedFilePath(this, "/"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {

		FTPClient ftp = connect();
		String currentDirectory = null;
		try {
			currentDirectory = ftp.printWorkingDirectory();
			if (path.getFileName() == null){
				ftp.changeWorkingDirectory(path.toString());
				FTPFile[] files = ftp.listFiles();
				for (FTPFile f : files){
					if (f.getName().equals("."))
					{
						return new FtpManagedFile(this, f, path);
					}
				}
				return new FtpManagedFile(this, null, path);
			}
			else 
			{
				if (ftp.changeWorkingDirectory(path.getParent().toString()))
				{
					FTPFile[] files = ftp.listFiles();
					for (FTPFile f : files){
						if (f.getName().equals(path.getFileName()))
						{
							return new FtpManagedFile(this, f, path);
						}
					}
				} else if (ftp.getReplyCode() == 550 /* Not found */){
					if (ftp.changeWorkingDirectory(path.toString()))
					{
						FTPFile[] files = ftp.listFiles();
						for (FTPFile f : files){
							if (f.getName().equals("."))
							{
								return new FtpManagedFile(this, f, path);
							}
						}
					} 
				}

				return new FtpManagedFile(this, null, path);
			}
			
		} catch (IOException e){
			throw  ManagedIOException.manage(e);
		} finally {
			if (currentDirectory != null){
				try {
					ftp.changeWorkingDirectory(currentDirectory);
				} catch (IOException e) {
					throw  ManagedIOException.manage(e);
				}
			}
		}
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
	public boolean isWriteable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWatchable() {
		return false;
	}

	protected class FtpManagedFile extends AbstractManagedFile implements AutoInputStreamCopy, AutoOutputStreamCopy {

		private ManagedFilePath path;
		private FTPFile file;

		protected FtpManagedFile(ManagedFileRepository repository, FTPFile file, ManagedFilePath path){
			super(repository);
			this.path = path;
			this.file = file;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ManagedFilePath getPath() {
			return path;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean exists() {
			if (file == null){
				return false;
			} else if (file.isDirectory()) {
				try {
					FTPClient ftp = connect();

					ftp.changeWorkingDirectory(path.toString());
					return !(ftp.getReplyCode() == 550);
				} catch (IOException e){
					throw  ManagedIOException.manage(e);
				}
			} else if (file.isFile()){
				try {
					FTPClient ftp = connect();
					return ftp.listFiles(path.toString()).length > 0;
				
				} catch (IOException e){
					throw  ManagedIOException.manage(e);
				}

			}
			return this.getRepository().retrive(path).exists();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isWatchable() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ManagedFileType getType() {
			if (file == null){
				return ManagedFileType.VIRTUAL;
			}
			return file.isDirectory() ? ManagedFileType.FOLDER : (file.isFile() || file.isSymbolicLink() ? ManagedFileType.FILE : ManagedFileType.VIRTUAL); 
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public URI getURI() {
			return URI.create("ftp://" + ftpCredentials.getFtpAddress() + path.toString());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
			return retrive(path.toString());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isReadable() {
			return file != null && file.hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isWriteable() {
			return file == null || file.hasPermission(FTPFile.USER_ACCESS, FTPFile.WRITE_PERMISSION);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean delete() {
			if (file != null){
				if (file.isDirectory()){
					try {
						FTPClient ftp = connect();

						ftp.rmd(path.toString());

					} catch (IOException e){
						throw  ManagedIOException.manage(e);
					}
				}
				else if (file.isFile())
				{
					try {
						FTPClient ftp = connect();

						ftp.deleteFile(path.toString());

					} catch (IOException e){
						throw  ManagedIOException.manage(e);
					}
				}
			}
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void autoCopyFrom(InputStream in) {
			try {
				FTPClient ftp = connect();

				ftp.storeFile(this.getPath().toString(), in);
				in.close();

				FtpManagedFile newFile = (FtpManagedFile)this.getRepository().retrive(this.getPath());
				this.file = newFile.file;
				
			} catch (IOException e){
				throw  ManagedIOException.manage(e);
			}
			
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void autoCopyTo(OutputStream out) {
			try {
				FTPClient ftp = connect();
				InputStream in = new BufferedInputStream(ftp.retrieveFileStream(path.toString()));

				IOTransport.copy(in).to(out);
				ftp.completePendingCommand();

			} catch (IOException e){
				throw  ManagedIOException.manage(e);
			}

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public StreamableContent doGetContent() {
			return new StreamableContentAdapter (){ // TODO check for read and  write permissions

				@Override
				public InputStream resolveInputStream() throws ManagedIOException {

					try {
						FTPClient ftp = connect();
						InputStream in = new BufferedInputStream(ftp.retrieveFileStream(path.toString()));

						ArrayByteBuffer buffer = new ArrayByteBuffer(ftp.getBufferSize());
						OutputStream out = buffer.getOutputStream();

						IOTransport.copy(in).to(out);

						ftp.completePendingCommand();

						return buffer.getInputStream();
					} catch (IOException e){
						throw  ManagedIOException.manage(e);
					}

				}

				@Override
				public OutputStream resolveOutputStream() throws ManagedIOException {
					return new CloseDetectOutputStream(FtpManagedFile.this);

				}

				@Override
				public long getSize() throws ManagedIOException {
					return FtpManagedFile.this.file.getSize();
				}

				@Override
				public boolean isReadable() {
					return FtpManagedFile.this.isReadable();
				}

				@Override
				public boolean isWritable() {
					return FtpManagedFile.this.isWriteable();
				}

			};

		}

		private class CloseDetectOutputStream extends ByteArrayOutputStream{

			private FtpManagedFile ftpManagedFile;

			public CloseDetectOutputStream(FtpManagedFile ftpManagedFile){
				this.ftpManagedFile = ftpManagedFile;
			}

			public void close() throws IOException{
				super.close(); // dump the inner buffer to the array

				FTPClient ftp = connect();

				InputStream in = new ByteArrayInputStream(this.buf);
				ftp.storeFile(ftpManagedFile.getPath().toString(), in);
				in.close();

				FtpManagedFile newFile = (FtpManagedFile)ftpManagedFile.getRepository().retrive(ftpManagedFile.getPath());
				ftpManagedFile.file = newFile.file;
				
			}
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ManagedFile doRetriveFromFolder(String path) {
			try {
				FTPClient ftp = connect();

				if (ftp.changeWorkingDirectory(this.path.toString())){
					FTPFile[] files = ftp.listFiles(path);

					if (files.length == 0){
						return new FtpManagedFile(this.getRepository(), null, this.getPath().resolve(path));
					}
					return new FtpManagedFile(this.getRepository(), files[0], this.getPath().resolve(path));
				}
				return new FtpManagedFile(this.getRepository(), null, this.getPath().resolve(path));
			} catch (IOException e){
				throw  ManagedIOException.manage(e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void doRenameAndChangePath(ManagedFilePath path) {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected boolean doContains(ManagedFile other) {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ManagedFile doCreateFile() {
			try {
				FTPClient ftp = connect();

				InputStream in = new ByteArrayInputStream(new byte[0]);
				ftp.storeFile(this.path.toString(), in);
				in.close();

				return this.getParent().retrive(this.path.getFileName());
			} catch (IOException e){
				throw  ManagedIOException.manage(e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ManagedFile doCreateFolder(ManagedFile parent) {
			try {
				FTPClient ftp = connect();
				
				if (ftp.makeDirectory(this.path.toString())){
					return parent.retrive(this.path.getFileName());
				}
				throw  new ManagedIOException(Joiner.with("\n").join(ftp.getReplyStrings()));
				
			} catch (IOException e){
				throw  ManagedIOException.manage(e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Iterable<ManagedFile> childrenIterable() {
			
			try {
				FTPClient ftp = connect();

				FTPFile[] files = ftp.listFiles(this.path.toString());
				
				List<ManagedFile> result = new ArrayList<ManagedFile>(files.length);
				for (FTPFile f : files){
					if (!f.getName().equals(".") && !f.getName().equals("..")){
						result.add(new FtpManagedFile(this.getRepository(), f, this.path.resolve(f.getName())));
					}
				}
				return result;
			} catch (IOException e){
				throw  ManagedIOException.manage(e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected int childrenCount() {
			if (file.isFile()){
				return 0;
			} else {
				FTPClient ftp = connect();

				try {
					return ftp.list(file.getName());
				} catch (IOException e){
					throw  ManagedIOException.manage(e);
				}
			}

		}

	}


}
