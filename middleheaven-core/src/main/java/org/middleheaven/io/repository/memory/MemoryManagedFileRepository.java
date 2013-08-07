/**
 * 
 */
package org.middleheaven.io.repository.memory;

import java.io.IOException;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedRepository;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.watch.WatchService;

/**
 * 
 */
public class MemoryManagedFileRepository extends AbstractManagedRepository implements ManagedFileRepository {

	private ManagedFile root = MemoryFile.folder("/", this);
	
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
	public boolean isWatchable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WatchService getWatchService() {
		throw new UnsupportedOperationException(); // TODO create memory watachable
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return root.retrive(path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<ManagedFilePath> getRoots() {
		return Collections.<ManagedFilePath>singleton(new ArrayManagedFilePath(this, "/"));
	}

}
