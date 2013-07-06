package org.middleheaven.io.repository.memory;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.repository.AbstractContainerManagedFile;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;

class MemoryFolder extends AbstractContainerManagedFile  {

	private final Map<String,ManagedFile> files = new TreeMap<String,ManagedFile>();
	private final ManagedFile parent;
	private String name;
	private ManagedFilePath path;
	
	public MemoryFolder(String name, ManagedFileRepository repository){
		super(repository);
		this.name = name;
		this.parent = null;
		this.path = new ArrayManagedFilePath(repository, name);
	}
	
	
	@Override
	public void copyTo(ManagedFile other) throws ManagedIOException {
		// TODO implement
		throw new UnsupportedOperationException();
	}

	@Override
	public ManagedFile doCreateFile() {
		// no-op
		return this;
	}

	@Override
	public ManagedFile doCreateFolder() {
		// no-op
		return this;
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public StreamableContent getContent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getSize() throws ManagedIOException {
		long sum =0;
		for (ManagedFile file : this.files.values()){
			sum+= file.getSize();
		}
		return sum;
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.FOLDER;
	}

	@Override
	public URI getURI() {
		// TODO implement ManagedFile.getURL
		return null;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWatchable() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return true;
	}

	@Override
	public Enumerable<ManagedFile> children() throws ManagedIOException {
		return CollectionUtils.asEnumerable(files.values());
	}


	@Override
	public ManagedFile retrive(String filepath) {
		return this.files.get(filepath);
	}

	public void add(ManagedFile file) {
		this.files.put(file.getPath().getLastName(),file);
		
	}

	public void remove(ManagedFile file) {
		this.files.remove(file.getPath().getLastName());
	}

	public void clear() {
		for (ManagedFile file : files.values()){
			file.delete();
		}
		files.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean doContains(ManagedFile other) {
		return files.containsValue(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doRetriveFromFolder(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRenameAndChangePath(ManagedFilePath resolveSibling) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Iterable<ManagedFile> childrenIterable() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int childrenCount() {
		return files.size();
	}







}
