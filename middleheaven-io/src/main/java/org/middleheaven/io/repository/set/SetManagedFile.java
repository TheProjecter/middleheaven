/**
 * 
 */
package org.middleheaven.io.repository.set;

import java.net.URI;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.BufferedMediaManagedFileContent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.empty.UnexistantManagedFile;

class SetManagedFile extends AbstractManagedFile {

	
	/**
	 * 
	 */
	private final SetManagedFileRepository setManagedFileRepository;
	private final BufferedMediaManagedFileContent content = new BufferedMediaManagedFileContent();
	private ManagedFilePath path;

	/**
	 * Constructor.
	 * @param repositoy
	 * @param path
	 * @param setManagedFileRepository TODO
	 */
	protected SetManagedFile(SetManagedFileRepository setManagedFileRepository, ManagedFileRepository repositoy, ManagedFilePath path) {
		super(repositoy);
		this.setManagedFileRepository = setManagedFileRepository;
		this.path  = path;
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
	protected void doRenameAndChangePath(ManagedFilePath newPath) {
		
		this.setManagedFileRepository.files.remove(this.getPath());
		
		this.setManagedFileRepository.files.put(newPath, this);
		
		this.path = newPath;
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean doContains(ManagedFile other) {
		return false;
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
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
	      return new UnexistantManagedFile(this.getRepository(), path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists() {
		return true;
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
	public URI getURI() {
		return null;
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
	public boolean isWriteable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete() {
		return  false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doCreateFile() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doCreateFolder(ManagedFile parent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileType getType() {
		return ManagedFileType.FILE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StreamableContent doGetContent() {
		return content;
	}


}