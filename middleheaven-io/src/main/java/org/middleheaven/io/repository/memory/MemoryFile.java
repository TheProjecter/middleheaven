package org.middleheaven.io.repository.memory;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.BufferedMediaManagedFileContent;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.ManagedFileType;

// TODO implement tests
class MemoryFile extends AbstractManagedFile  {

	private final Map<String,ManagedFile> files = new TreeMap<String,ManagedFile>();
	private final ManagedFile parent;

	private BufferedMediaManagedFileContent content = null;
	private ManagedFileType type;
	private boolean isCreated = false;
	private String name;
	
	protected MemoryFile( ManagedFileType type, MemoryFile parent, String name, ManagedFileRepository repository){
		super(repository);
		this.parent = parent;
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
	public ManagedFile doCreateFolder(ManagedFile parent) {
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
		return Enumerables.asEnumerable(files.values());
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

	private  ManagedFile virtual (MemoryFile parent, String name){
		return new MemoryFile(ManagedFileType.VIRTUAL, parent, name, this.getRepository());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return virtual(this, path.getFileName()); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile doRetriveFromFolder(String filepath) {
		ManagedFile file = this.files.get(filepath);
		
		if (file != null){
			return file;
		} else {
			return virtual(this, filepath);  
		}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StreamableContent doGetContent() {
		return content;
	}

}
