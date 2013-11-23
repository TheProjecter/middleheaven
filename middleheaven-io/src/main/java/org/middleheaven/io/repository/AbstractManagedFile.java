package org.middleheaven.io.repository;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.middleheaven.collections.enumerable.AbstractEnumerableAdapter;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.tree.TreeEnumerable;
import org.middleheaven.io.AutoInputStreamCopy;
import org.middleheaven.io.AutoOutputStreamCopy;
import org.middleheaven.io.IOTransport;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.ModificationNotTracableIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.StreamableContentSource;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.util.Splitter;
import org.middleheaven.util.function.Block;

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

    public final StreamableContent getContent() {
    	if (this.getType().isFile()){
    		return doGetContent();
    	} else {
    		throw new ManagedIOException("Type " + this.getType() + " has no streamable content");
    	}
    }
    
	protected abstract StreamableContent doGetContent();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void deleteTree() {

		if (this.getType().isFolder()) {
			for (ManagedFile file : this.childrenIterable()){
				file.deleteTree();
			}
		}
		this.delete();
	}


	@Override
	public ManagedFile getParent() {
		return this.getRepository().retrive(this.getPath().getParent());
	}

	@Override
	public final long getSize() {
		return this.repository.resolveFileSize(this);
	}
	


	/**
	 * 
	 * {@inheritDoc}
	 */
	public  ManagedFile retrive(String path) throws ManagedIOException {
		switch (this.getType()){
		case VIRTUAL:
		case FOLDER:
		case FILEFOLDER:
			Deque<String> deque = new LinkedList<String>();
			for (String s : Splitter.on('/').split(path)){
				deque.addLast(s);
			}	
			ManagedFile file = doRetriveFromFolder(deque.pop());
			while(!deque.isEmpty())
			{
				file = file.retrive(deque.pop());
			}
			return file ;
		case FILE:
		default:
			throw new ManagedIOException("Cannot retrive a path from inside a file");
		}
	}


	/**
	 * Finds and retrives a path inside this managedfile when its a Folder.
	 * @param path
	 * @return
	 */
	protected abstract ManagedFile doRetriveFromFolder(String path);

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final void copyTo(ManagedFile other) throws ManagedIOException {
		if (this.getType().isFile() && other.getType().isFile()){
			doCopy(this,other);
		} else if (this.getType().isFile() && other.getType().isFolder()){
			ManagedFile newFile = other.retrive(this.getPath()).createFile();
			doCopy(this,newFile);
		} else if (this.getType().isFolder() && other.getType().isFolder()){
			// TODO test
			for(ManagedFile file : this.children()){
				ManagedFile otherFile = other.createFile();
				doCopy(file,otherFile);
			}
		} else {
			throw new ManagedIOException("Cannot copy form " + this.getType() +" to " + other.getType());
		}

	}

	
	private static void doCopy(ManagedFile source, ManagedFile target){
		if (target instanceof AutoInputStreamCopy){
			AutoInputStreamCopy a = (AutoInputStreamCopy) target;
			a.autoCopyFrom(source.getContent().getInputStream());
		} else if (source instanceof AutoOutputStreamCopy ){
			AutoOutputStreamCopy a = (AutoOutputStreamCopy) source;
			a.autoCopyTo(target.getContent().getOutputStream());
		} else {
			IOTransport.copy(source).to(target);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void copyTo(StreamableContentSource other) throws ManagedIOException{
		IOTransport.copy(this).to(other);
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
	public final ManagedFile createFile() {
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
	
	@Override
	public final ManagedFile createFolder() {
		switch (this.getType()){
		case FOLDER:
		case FILEFOLDER:
			return this;
		case VIRTUAL:
			ManagedFile parent = this.getParent();
			if (!parent.exists()){
				parent.createFolder();
			}
			return doCreateFolder(parent);
		default:
			throw new UnsupportedOperationException("Cannot create folder of type " + this.getType());
		}
	}

	protected abstract ManagedFile doCreateFile();
	protected abstract ManagedFile doCreateFolder(ManagedFile parent);

	
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
	 * {@inheritDoc}
	 */
	@Override
	public boolean isModificationTraceable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedModificationTrace getModificationTrace()
			throws ModificationNotTracableIOException {
		throw new ModificationNotTracableIOException();
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
