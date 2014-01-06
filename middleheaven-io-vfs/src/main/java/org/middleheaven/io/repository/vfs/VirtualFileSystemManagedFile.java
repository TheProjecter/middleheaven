package org.middleheaven.io.repository.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.util.function.Predicate;

final class VirtualFileSystemManagedFile extends AbstractManagedFile implements ManagedFileRepository{

	protected final FileObject file;
	private final String finalPath;
	private boolean isfilefolder;
	
	private VirtualFileWatchService watchService;

	VirtualFileSystemManagedFile(VirtualFileWatchService watchService, FileObject file){
		super(null);
		this.watchService = watchService;
		this.file = file;
		try {	
			if (file.getType().hasContent()){
				final String ext = file.getName().getExtension();
				if (ext.equals("jar") || ext.equals("zip") || ext.equals("tar") || ext.equals("tgz")){
					finalPath = ext + ":" + file.getName().getURI();
					isfilefolder = true;
				} else {
					isfilefolder = false;
					finalPath = file.getName().getURI();
				}
			} else {
				isfilefolder = false;
				finalPath = file.getName().getURI();
			}
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}

	}

	public String toString(){
		return file.toString();
	}

	/**
	 *
	 * @return list of the ManagedFile existing in the repository 
	 */
	public Enumerable<ManagedFile> children() throws ManagedIOException{

		if (this.getType() == ManagedFileType.FILE || !this.exists()){
			return Enumerables.emptyEnumerable();
		}

		try {
			FileObject[] files = getVirtualFolder().getChildren();
			Collection<ManagedFile> mfiles = new ArrayList<ManagedFile>();

			for (FileObject fo : files){
				mfiles.add(new VirtualFileSystemManagedFile(watchService,fo));
			}

			return Enumerables.asEnumerable(mfiles);
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}

	}



	public Collection<? extends  ManagedFile> listFiles(Predicate<ManagedFile> filter) throws ManagedIOException{
		if (this.getType() == ManagedFileType.FILE || !this.exists()){
			return Collections.emptySet();
		}

		try {

			FileObject[] files = getVirtualFolder().getChildren();
			Collection<ManagedFile> mfiles = new ArrayList<ManagedFile>();

			for (FileObject fo : files){
				ManagedFile mf = new VirtualFileSystemManagedFile(watchService,fo);
				if (filter.apply(mf)){
					mfiles.add(mf);
				}
			}

			return mfiles;
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	private FileObject getVirtualFolder() throws FileSystemException{
		if (this.getType() == ManagedFileType.FOLDER){
			return this.file;
		} else {
			return VFS.getManager().resolveFile(this.finalPath);
		}
	}

	@Override
	public ManagedFileType getType() {
		try {



			if (file.getType().hasContent()){
				if (this.isfilefolder || file.getType().hasChildren()){
					return ManagedFileType.FILEFOLDER;
				} else {
					return ManagedFileType.FILE;
				}
			} else {
				if(file.exists()){
					return ManagedFileType.FOLDER;
				} else {
					return ManagedFileType.VIRTUAL;
				}
			}

		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public ManagedFile retrive(String filepath) {
		try{
			return new VirtualFileSystemManagedFile(watchService,getVirtualFolder().resolveFile(filepath));
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public boolean isReadable() {
		try {
			return file.isReadable();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	public boolean isWriteable() {
		try {
			return file.isWriteable();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}


	@Override
	public URI getURI() {
		try {
			return file.getURL().toURI();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		} catch (URISyntaxException e) {
			throw new VirtualFileSystemException(e);
		}
	}




	@Override
	public boolean exists() {
		try {
			return this.file.exists();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	@Override
	protected StreamableContent doGetContent() {
		return new VirtualManagedFileContent();
	}

	@Override
	public ManagedFile getParent() {
		try{
			return new VirtualFileSystemManagedFile(watchService,file.getParent());
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}


	@Override
	public boolean equals(Object other){
		return  (other instanceof VirtualFileSystemManagedFile) && ((VirtualFileSystemManagedFile)other).file.equals(this.file);
	}

	@Override
	public int hashCode(){
		return this.file.hashCode();
	}

	@Override
	public boolean delete() {
		try{
			return this.file.delete();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}



	private class VirtualManagedFileContent implements  StreamableContent{

		@Override
		public InputStream getInputStream() throws ManagedIOException{
			try {
				return file.getContent().getInputStream();
			} catch (FileSystemException e) {
				throw new VirtualFileSystemException(e);
			}
		}

		@Override
		public OutputStream getOutputStream() throws ManagedIOException{
			try {
				return file.getContent().getOutputStream();
			} catch (FileSystemException e) {
				throw new VirtualFileSystemException(e);
			}
		}

		@Override
		public long getSize() throws ManagedIOException {
			return VirtualFileSystemManagedFile.this.getSize();
		}

		@Override
		public boolean setSize(long size) throws ManagedIOException {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isReadable() {
			try {
				return file.isReadable();
			} catch (FileSystemException e) {
				throw new VirtualFileSystemException(e);
			}
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
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isWritable() {
			try {
				return file.isWriteable();
			} catch (FileSystemException e) {
				throw new VirtualFileSystemException(e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isContentTypeWritable() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setContentType(String contentType)
				throws ManagedIOException {
			throw new UnsupportedOperationException("Not implememented yet");
		}

	}

	@Override
	public boolean isWatchable() {
		return this.getType().isFolder();
	}

	@Override
	protected ManagedFile doCreateFile() {
		try{
			this.file.createFile();
			return this;
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}

	}

	@Override
	protected ManagedFile doCreateFolder(ManagedFile parent) {
		try{
			this.file.createFolder();
			return this;
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean doContains(ManagedFile other) {
		return other.getParent().equals(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doRetriveFromFolder(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRenameAndChangePath(ManagedFilePath resolveSibling) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Iterable<ManagedFile> childrenIterable() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int childrenCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<ManagedFilePath> getRootPaths() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WatchService getWatchService() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(ManagedFilePath path) throws ManagedIOException {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete(ManagedFilePath path) throws ManagedIOException {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath getPath(String first, String... more) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile getFirstRoot() {
		return this.retrive(this.getRootPaths().iterator().next());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long resolveFileSize(ManagedFile managedFile) {
		try {
			return ((VirtualFileSystemManagedFile)managedFile).file.getContent().getSize();
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

}
