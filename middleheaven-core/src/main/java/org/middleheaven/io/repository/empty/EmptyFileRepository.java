package org.middleheaven.io.repository.empty;

import java.io.IOException;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.RepositoryNotWritableException;
import org.middleheaven.io.repository.watch.WatchService;

public class EmptyFileRepository implements ManagedFileRepository {

	private static final EmptyFileRepository me = new EmptyFileRepository();
	
	public static EmptyFileRepository repository(){
		return me;
	}

	private EmptyFileRepository(){}
	

	@Override
	public boolean delete(ManagedFilePath path) throws ManagedIOException {
		throw new RepositoryNotWritableException(this.getClass().getName());
	}
	
	@Override
	public boolean exists(ManagedFilePath path) throws ManagedIOException {
		return false;
	}

	@Override
	public boolean isReadable() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return new UnexistantManagedFile(null , path);
	}

	@Override
	public boolean isWatchable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		// no-op
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WatchService getWatchService() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<ManagedFilePath> getRoots() {
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath getPath(String first, String... more) {
		return new ArrayManagedFilePath(this, first, more);
	}

}
