package org.middleheaven.io.repository.empty;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractMediaManagedFile;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.MediaManagedFileContent;
import org.middleheaven.io.repository.RepositoryNotWritableException;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.Predicate;
import org.middleheaven.util.collections.Walkable;

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

	private final static AllwaysEmpyMediaManagedFileContent content = new AllwaysEmpyMediaManagedFileContent();
	
	public static class UnexistantManagedFile extends AbstractMediaManagedFile{

		private ManagedFilePath path;
		
		public UnexistantManagedFile(ManagedFileRepository repository, ManagedFilePath path){
			super(repository);
			this.path = path;
		}
		

		@Override
		public void copyTo(ManagedFile other) throws ManagedIOException {
			// no-op
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
		public MediaManagedFileContent getContent() {
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


		

	


	}

}
