/**
 * 
 */
package org.middleheaven.io.repository.empty;

import java.util.Collections;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractMediaManagedFile;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.MediaStreamableContent;

public class UnexistantManagedFile extends AbstractMediaManagedFile{
	
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
	public MediaStreamableContent getContent() {
		return content;
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.VIRTUAL;
	}

	@Override
	public ManagedFile retrive(String path) throws ManagedIOException {
		return new UnexistantManagedFile(this.getRepository(), this.getPath().resolve(path));
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
		return null;
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


	




}