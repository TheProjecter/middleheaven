package org.middleheaven.io.repository.machine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

import org.middleheaven.collections.TransformedCollection;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.ModificationNotTracableIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.StreamableContentAdapter;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.ManagedModificationTrace;
import org.middleheaven.io.repository.empty.EmptyFileContent;
import org.middleheaven.io.repository.watch.Watchable;
import org.middleheaven.quantity.time.EpocTimePoint;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.util.function.Function;

/**
 * 
 */
class FileIOManagedFileAdapter  extends AbstractManagedFile implements Watchable {

    File systemFile;
	private final MachineIOSystemManagedFileRepository repository;
	private ManagedFilePath path;

	/**
	 * 
	 * Create a {@link FileIOManagedFileAdapter} based on a {@link java.io.File}.
	 * 
	 * @param accessStrategy the accessStrategy in use.
	 * @param parent the parent file.
	 * @param base the pivot file.
	 */
	public FileIOManagedFileAdapter(MachineIOSystemManagedFileRepository repository, File systemFile , ManagedFilePath path) {
		super(repository);
		this.repository = repository;
		this.systemFile = systemFile;
		this.path = path;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath getPath() {
		return path;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean equals(Object other){
		return (other instanceof FileIOManagedFileAdapter) && ((FileIOManagedFileAdapter)other).systemFile.equals(this.systemFile);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode (){
		return systemFile.hashCode();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString(){
		return systemFile.toString();
	}
	
	@Override
	public boolean delete() {
		return systemFile.delete();
	}

	@Override
	public boolean exists() {
		return systemFile.exists();
	}

	@Override
	protected StreamableContent doGetContent() {
		if (this.getType().equals(ManagedFileType.FOLDER)){
			return EmptyFileContent.getInstance(); 
		}

		return new StreamableContentAdapter(){

			@Override
			protected InputStream resolveInputStream() throws ManagedIOException {
				try {
					return new BufferedInputStream(new FileInputStream(systemFile));
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}

			@Override
			protected OutputStream resolveOutputStream() throws ManagedIOException {
				try {
					return new BufferedOutputStream(new FileOutputStream(systemFile));
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}


			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean isWritable() {
				return systemFile.canWrite();
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean isReadable() {
				return systemFile.canRead();
			}
			
			@Override
			public long getSize() throws ManagedIOException {
				return FileIOManagedFileAdapter.this.getSize();
			}

			@Override
			public boolean setSize(long size) throws ManagedIOException {
				// not supported. fail silently
				return false;
			}

		};
	}
	

	@Override
	public ManagedFileType getType() {
		return this.systemFile.exists() ? ( this.systemFile.isFile() ? ManagedFileType.FILE : ManagedFileType.FOLDER) : ManagedFileType.VIRTUAL;
	}

	@Override
	public URI getURI() {
		return systemFile.toURI();
	}

	@Override
	public boolean isReadable() {
		return systemFile.canRead();
	}

	@Override
	public boolean isWatchable() {
		return true;
	}

	@Override
	public boolean isWriteable() {
		return systemFile.canWrite();
	}

	@Override
	protected Iterable<ManagedFile> childrenIterable() throws ManagedIOException {
		
		File[] children = systemFile.getAbsoluteFile().listFiles();

		if (children == null){ // not a folder
			return Collections.emptySet();
		} else {
			return TransformedCollection.transform(Arrays.asList(children), new Function<ManagedFile , File> (){

				@Override
				public ManagedFile apply(File obj) {
					return repository.resolveFile(obj);
				}

			}
			);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int childrenCount() {
		String[] childrenNames = systemFile.list();

		if (childrenNames == null){ // not a folder
			return 0;
		} else {
			return childrenNames.length;
		}
	}

	protected ManagedFile doRetriveFromFolder(String path){
		return repository.retrive(this.getPath().resolve(path));
	}

	@Override
	protected ManagedFile doCreateFile() {
		this.repository.createFile(this.systemFile);
		return this.getParent().retrive(this.path.getFileName());

	}

	@Override
	protected ManagedFile doCreateFolder(ManagedFile parent) {
		this.repository.createFolder(this.systemFile);
		return this.getParent().retrive(this.path.getFileName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean doContains(ManagedFile other) {
		ManagedFile found = repository.retrive( other.getPath().relativize(this.path));
		return found != null && found.exists();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return repository.retrive(this.getPath().resolve(path));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRenameAndChangePath(ManagedFilePath resolveSibling) {
		final File newFile = new File(systemFile.getParent(), resolveSibling.getLastName());
		this.systemFile.renameTo(newFile);
		
		this.path = repository.pathForFile(newFile);
		this.systemFile = newFile;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isModificationTraceable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedModificationTrace getModificationTrace() throws ModificationNotTracableIOException {
		return new ManagedModificationTrace(){

			@Override
			public TimePoint getLastModified() {
				return new EpocTimePoint(systemFile.lastModified());
			}

			@Override
			public void setLastModified(TimePoint timePoint) {
				systemFile.setLastModified(timePoint.getMilliseconds());
			}
			
		};
	}


}
