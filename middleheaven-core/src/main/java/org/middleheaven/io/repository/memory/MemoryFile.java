package org.middleheaven.io.repository.memory;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.io.IOTransport;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.repository.AbstractContainerManagedFile;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.BufferedMediaManagedFileContent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;

// TODO implement tests
class MemoryFile extends AbstractContainerManagedFile  {

	private final Map<String,ManagedFile> files = new TreeMap<String,ManagedFile>();
	private final ManagedFile parent;

	private BufferedMediaManagedFileContent content = null;
	private ManagedFileType type;
	private boolean isCreated = false;
	private String name;
	
	public static ManagedFile folder (String name, ManagedFileRepository repository){
		return new MemoryFile(ManagedFileType.FOLDER, null, name, repository);
	}
	
	private static ManagedFile virtual (MemoryFile parent, String name, ManagedFileRepository repository){
		return new MemoryFile(ManagedFileType.VIRTUAL, parent, name, repository);
	}
	
	private MemoryFile( ManagedFileType type, MemoryFile parent, String name, ManagedFileRepository repository){
		super(repository);
		this.parent = null;
		this.type = type;
		this.name = name;
	}
	
	@Override
	public ManagedFile doCreateFile() {
		this.type = ManagedFileType.FILE;
		this.content = new BufferedMediaManagedFileContent();
		this.isCreated = true;
		return this;
	}

	@Override
	public ManagedFile doCreateFolder() {
		this.type = ManagedFileType.FOLDER;
		this.isCreated = true;
		return this;
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean exists() {
		return isCreated;
	}

	@Override
	public StreamableContent getContent() {
		if (type == ManagedFileType.FILE){
			return content;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public long getSize() throws ManagedIOException {
		switch (type){
		case FILE:
			return  this.content.getSize();
		case FOLDER:
			long sum =0;
			for (ManagedFile file : this.files.values()){
				sum+= file.getSize();
			}
			return sum;
		case VIRTUAL:
		case FILEFOLDER:
		default:
			return 0;
		}
	}

	@Override
	public ManagedFileType getType() {
		return type;
	}

	@Override
	public URI getURI() {
	    return URI.create("mem:/" + this.getPath().toString());
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
		ManagedFile file = this.files.get(filepath);
		
		if (file != null){
			return file;
		} else {
			return MemoryFile.virtual(this, filepath, this.getRepository());  
		}
		
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
		if ( this.parent == null){
			return new ArrayManagedFilePath(this.getRepository(), name);
		} else {
			return this.parent.getPath().resolve(name);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return MemoryFile.virtual(this, path.getFileName(), this.getRepository()); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doRetriveFromFolder(String path) {
		return files.get(path);
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
		return this.files.values();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int childrenCount() {
		return files.size();
	}

}
