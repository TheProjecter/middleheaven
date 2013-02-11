package org.middleheaven.io.repository.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;

import org.apache.commons.fileupload.FileItem;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractMediaManagedFile;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.io.repository.MediaManagedFileContent;
import org.middleheaven.io.repository.empty.UnexistantManagedFile;

/**
 * 
 */
class UploadManagedFile extends AbstractMediaManagedFile implements MediaManagedFile {

	private final FileItem fileItem;
	private final ManagedFile parent;
	private ManagedFilePath path;
	
	UploadManagedFile(ManagedFileRepository repository, FileItem fileItem, ManagedFile parent){
		super(repository);
		this.fileItem = fileItem;
		this.parent = parent;
		this.path =  new ArrayManagedFilePath(parent.getPath() , fileItem.getFieldName());
	}

	public boolean equals(Object other){
		return other instanceof UploadManagedFile && equalsOther((UploadManagedFile)other);
	}
	
	private boolean equalsOther(UploadManagedFile other){
		return other.fileItem.equals(this.fileItem);
	}
	
	public int hashCode(){
		return this.fileItem.hashCode();
	}

	@Override
	public boolean delete() {
		fileItem.delete();
		return true;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public MediaManagedFileContent getContent() {
		return new FileItemManagedFileContent();
	}


	/**
	 * Names are not mutable for this class
	 */
	public boolean canRenameTo(String newName){
		return false;
	}

	@Override
	public ManagedFile getParent() {
		return parent;
	}

	@Override
	public URI getURI() {
		return URI.create(fileItem.getFieldName());
	}


	private class FileItemManagedFileContent implements  MediaManagedFileContent{

		@Override
		public InputStream getInputStream() throws ManagedIOException{

			try {
				return fileItem.getInputStream();
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}

		}

		@Override
		public OutputStream getOutputStream() throws ManagedIOException{
			try {
				return fileItem.getOutputStream();
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}
		}

		@Override
		public long getSize() throws ManagedIOException {
			return fileItem.getSize();
		}

		@Override
		public boolean setSize(long size) throws ManagedIOException {
			return false;
		}

		@Override
		public String getContentType() {
			return fileItem.getContentType();
		}

		@Override
		public void setContentType(String contentType) {
			// not supported. fail silently
		}

	}

	@Override
	public long getSize() throws ManagedIOException {
		return fileItem.getSize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean doContains(ManagedFile other) {
		throw new ManagedIOException("unsuppported operation");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doRetriveFromFolder(String path) {
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
	public ManagedFilePath getPath() {
		return path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Iterable<ManagedFile> childrenIterable() {
		return Collections.<ManagedFile>emptySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int childrenCount() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTree() {
		//no-op. there is no tree.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return new UnexistantManagedFile(this.getRepository(), path);
	}





}
