package org.middleheaven.io.repository.set;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedRepository;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;

/**
 * {@link ManagedFileRepository} backed by a set for {@link ManagedFile}s.
 * 
 * This repository cannot contain <code>ManagedFile</code>s of <code>FOLDER</code> type.
 */
public class SetManagedFileRepository extends AbstractManagedRepository  {

	Map<ManagedFilePath , ManagedFile> files = new HashMap<ManagedFilePath,ManagedFile>();

	
	public static SetManagedFileRepository newInstance(){
		return new SetManagedFileRepository();
	}
	
	private SetManagedFileRepository(){

	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWriteable() {
		return true;
	}

	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		
		if (path.getNameCount() > 1){
			throw new IllegalArgumentException( this.getClass() + " acepts only one level for paths.");
		}
		ManagedFile file = files.get(path);
		if (file==null){
			// use Media file for compatibility with any file that would be stored
			file = new SetManagedFile(this, this, path);
			this.files.put(path, file);
		}
		return file;
	}


	@Override
	public boolean isWatchable() {
		return false;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<ManagedFilePath> getRootPaths() {
		return Collections.<ManagedFilePath>singleton(new ArrayManagedFilePath(this,"/"));
	}


	/**
	 * 
	 */
	public void clear() {
		this.files.clear();
	}
}
