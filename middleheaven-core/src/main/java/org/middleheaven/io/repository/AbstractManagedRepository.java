package org.middleheaven.io.repository;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.util.function.Maybe;

/**
 * Default implementation for some methods of {@link ManagedFileRepository}.
 */
public abstract class AbstractManagedRepository implements ManagedFileRepository {

	@Override
	public final boolean exists(ManagedFilePath path) throws ManagedIOException {
		return retrive(path).exists();
	}
	
	@Override
	public final boolean delete(ManagedFilePath path) throws ManagedIOException {
		return delete(retrive(path));
	}

	public boolean delete(ManagedFile file) throws ManagedIOException {
		
		Maybe<ManagedFileRepository> repo = safeCast(file.getParent(), ManagedFileRepository.class);
		
		if (repo.isAbsent() || !repo.get().equals(this)){
			return false;
		}
		return file.delete();
	}

	public void store(ManagedFile file) throws RepositoryNotWritableException, ManagedIOException {
		Maybe<ManagedFileRepository> repo = safeCast(file.getParent(), ManagedFileRepository.class);
		
		if (repo.isAbsent() || repo.get().equals(this)){
			// already in store
			return;
		}
		
		if (!this.isWriteable()){
			throw new RepositoryNotWritableException(this.getClass().getName());
		}
		
		ManagedFile myFile = this.retrive(file.getPath());
		if (!myFile.exists()){
			myFile = myFile.createFile();
		}
		
		if (file instanceof MediaManagedFile && myFile instanceof MediaManagedFile){
			((MediaManagedFile) myFile).getContent().setContentType(((MediaManagedFile)file).getContent().getContentType());
			((MediaManagedFile) myFile).getContent().setSize(((MediaManagedFile)file).getContent().getSize());
		}
		
		file.copyTo(myFile);
		
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath getPath(String first, String... more) {
		return new ArrayManagedFilePath(this, first, more);
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
	public void close() throws IOException {
		// no-op
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WatchService getWatchService() {
		if (!isWatchable()){
			throw new UnsupportedOperationException(" This repository does not support file watching");
		} else {
			throw new UnsupportedOperationException(" You should override getWatchService()");
		}
	}


}
