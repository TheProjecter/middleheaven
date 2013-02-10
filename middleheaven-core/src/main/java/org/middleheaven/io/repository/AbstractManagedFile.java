package org.middleheaven.io.repository;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.middleheaven.io.IOTransport;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.util.collections.AbstractEnumerableAdapter;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.collections.IterableEnumerable;
import org.middleheaven.util.collections.Pair;
import org.middleheaven.util.collections.PairEnumerable;
import org.middleheaven.util.collections.TreeEnumerable;
import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

/**
 * Default implementation of a {@link ManagedFile} usefull for extention.
 */
public abstract class AbstractManagedFile implements ManagedFile {


	private ManagedFileRepository repository;


	protected AbstractManagedFile(ManagedFileRepository repository){
		this.repository = repository;
	}

	public ManagedFileRepository getRepository(){
		return repository;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTree() {

		if (this.getType().isFile()) {
			this.delete();
		} else {
			for (ManagedFile file : this.childrenIterable()){
				file.deleteTree();
			}
		}
	
	}


	@Override
	public ManagedFile getParent() {
		return this.getRepository().retrive(this.getPath().getParent());
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	public ManagedFile retrive(String path) throws ManagedIOException {
		switch (this.getType()){
		case FOLDER:
		case FILEFOLDER:
			return doRetriveFromFolder(path);
		case VIRTUAL:
			return this;
		case FILE:
		default:
			//no-op
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		throw new UnsupportedOperationException("Not implemented yet");
	}


	/**
	 * @param path
	 * @return
	 */
	protected abstract ManagedFile doRetriveFromFolder(String path);

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void copyTo(ManagedFile other) throws ManagedIOException {
		try {
			if (other.getType()==ManagedFileType.FILE){
				IOTransport.copy(this.getContent().getInputStream()).to(other.getContent().getOutputStream());
			} else {
				ManagedFile newFile = other.retrive(this.getPath());
				newFile.createFile();
				IOTransport.copy(this.getContent().getInputStream()).to(newFile.getContent().getOutputStream());
			}

		} catch (IOException ioe) {
			throw ManagedIOException.manage(ioe);
		}
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void copyTo(ContentSource other) throws ManagedIOException{
		try {
			IOTransport.copy(this.getContent().getInputStream()).to(other.getContent().getOutputStream());
		} catch (IOException ioe) {
			throw ManagedIOException.manage(ioe);
		}
	}


	@Override
	public boolean canRenameTo(String newName) {
		ManagedFile p = this.getParent();
		if ( p == null){
			return false;
		}
		return !p.retrive(p.getPath().resolve(newName)).exists();
	}

	@Override
	public void renameTo(String newName) {
		if (canRenameTo(newName)) {
			doRenameAndChangePath(this.getPath().resolveSibling(newName));
		}
	}

	/**
	 * @param resolveSibling
	 */
	protected abstract void doRenameAndChangePath(ManagedFilePath path);



	@Override
	public ManagedFile createFile() {
		switch (this.getType()){
		case FILE:
		case FILEFOLDER:
			return this;
		case VIRTUAL:
			return doCreateFile();
		default:
			throw new UnsupportedOperationException("Cannot create file of type " + this.getType());
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public final boolean contains(ManagedFile other) {
		switch (this.getType()){
		case FILEFOLDER:
		case FOLDER:
			return doContains(other);
		default:
			return false;
		}
	}

	protected abstract boolean doContains(ManagedFile other);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final WatchEventChannel register(WatchService watchService, Kind... events) {
		if (this.isWatchable()){
			return watchService.watch(this, events);
		}

		throw new UnsupportedOperationException("This file is not watchable"); 
	}

	@Override
	public ManagedFile createFolder() {
		switch (this.getType()){
		case FOLDER:
		case FILEFOLDER:
			return this;
		case VIRTUAL:
			return doCreateFolder();
		default:
			throw new UnsupportedOperationException("Cannot create folder of type " + this.getType());
		}
	}




	protected abstract ManagedFile doCreateFile();
	protected abstract ManagedFile doCreateFolder();

	/**
	 * @return
	 */
	public Enumerable<ManagedFile> children(){
		return new ManagedFileEnumerable();
	}

	public TreeEnumerable<ManagedFile> asTreeEnumerable(){
		return new TreeEnumerable<ManagedFile>(){

			@Override
			public void forEachParent(Block<ManagedFile> walker) {
				
				final ManagedFile parent = getParent();
				if(parent!=null){
					walker.apply(parent);
					parent.asTreeEnumerable().forEachParent(walker);
				}
			}
		
			@Override
			public void forEachRecursive(Block<ManagedFile> walker) {
				for (ManagedFile file  : childrenIterable()){
					walker.apply(file);
					file.asTreeEnumerable().forEachRecursive(walker);
				}
			}
			
		};
	}

	/**
	 * @return
	 */
	protected abstract Iterable<ManagedFile> childrenIterable();

	protected abstract int childrenCount();


	private class ManagedFileEnumerable extends AbstractEnumerableAdapter<ManagedFile> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			return childrenCount();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEmpty() {
			return size() == 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<ManagedFile> iterator() {
			return childrenIterable().iterator();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ManagedFile getFirst() {
			return this.isEmpty() ? null : this.iterator().next();
		}


	}

}
