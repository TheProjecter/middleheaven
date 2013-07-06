package org.middleheaven.io.repository.set;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedRepository;
import org.middleheaven.io.repository.AbstractMediaManagedFile;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.BufferedMediaManagedFileContent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.MediaStreamableContent;
import org.middleheaven.io.repository.empty.UnexistantManagedFile;

/**
 * {@link ManagedFileRepository} backed by a set for {@link ManagedFile}s.
 * 
 * This repository cannot contain <code>ManagedFile</code>s of <code>FOLDER</code> type.
 */
public class SetManagedFileRepository extends AbstractManagedRepository  {

	private Map<ManagedFilePath , ManagedFile> files = new HashMap<ManagedFilePath,ManagedFile>();

	
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
			file = new SetManagedFile(this, path);
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
	public Iterable<ManagedFilePath> getRoots() {
		return Collections.<ManagedFilePath>singleton(new ArrayManagedFilePath(this,"/"));
	}


	private class SetManagedFile extends AbstractMediaManagedFile {

		
		private final BufferedMediaManagedFileContent content = new BufferedMediaManagedFileContent();
		private ManagedFilePath path;

		/**
		 * Constructor.
		 * @param repositoy
		 * @param path
		 */
		protected SetManagedFile(ManagedFileRepository repositoy, ManagedFilePath path) {
			super(repositoy);
			this.path  = path;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MediaStreamableContent getContent() {
			return content;
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
			
			files.remove(this.getPath());
			
			files.put(newPath, this);
			
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

	
	}


	/**
	 * 
	 */
	public void clear() {
		this.files.clear();
	}
}
