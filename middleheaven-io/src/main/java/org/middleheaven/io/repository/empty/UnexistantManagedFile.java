/**
 * 
 */
package org.middleheaven.io.repository.empty;

import java.net.URI;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;

public class UnexistantManagedFile extends AbstractManagedFile{
	
	private final static AllwaysEmpyMediaManagedFileContent content = new AllwaysEmpyMediaManagedFileContent();

	private ManagedFilePath path;
	
	public UnexistantManagedFile(ManagedFileRepository repository, ManagedFilePath path){
		super(repository);
		this.path = path;
	}
	
	@Override
	public boolean delete() {
		return true;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	protected StreamableContent doGetContent() {
		return content;
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.VIRTUAL;
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
	protected void doRenameAndChangePath(ManagedFilePath resolveSibling) {
		// no-op
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
	protected ManagedFile doRetriveFromFolder(String path) {
		return new UnexistantManagedFile(this.getRepository(), this.getPath().resolve(path));
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
	protected Iterable<ManagedFile> childrenIterable() {
		return Collections.emptySet();
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
	public boolean isWatchable() {
		throw new UnsupportedOperationException("Not implememented yet");
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
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWriteable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doCreateFile() {
		throw new UnsupportedOperationException("Cannot create an unexistante file");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doCreateFolder(ManagedFile parent) {
		throw new UnsupportedOperationException("Cannot create an unexistante folder");
	}

}