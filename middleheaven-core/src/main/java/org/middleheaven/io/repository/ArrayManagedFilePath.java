package org.middleheaven.io.repository;

import java.util.Arrays;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.io.repository.watch.Watchable;
import org.middleheaven.util.Joiner;
import org.middleheaven.util.Splitter;
import org.middleheaven.util.StringUtils;


/**
 * Simple implementation of {@link ManagedFilePath} based in an array.
 */
public class ArrayManagedFilePath implements ManagedFilePath , Watchable {

	private final String[] names;
	private final ManagedFileRepository repository;
	private final String root;
	private final String lastNameBase;
	private final String lastNameExtention;

	public ArrayManagedFilePath(ManagedFileRepository repository, String root ,  String ... more){
		this.repository = repository;
		this.root = root;
		this.names = more;

		if (names.length == 0){
			lastNameBase = root;
			lastNameExtention = "";
		} else {
			String lastName = this.names[names.length-1];
			int pos = lastName.lastIndexOf('.');
			if (pos < 0){
				lastNameExtention = "";
				lastNameBase = lastName;
			} else {
				lastNameBase = lastName.substring(0, pos);
				lastNameExtention = lastName.substring(pos+1);
			}

		}


	}

	public boolean isAbsolute(){
		return root != null;
	}
	
	
	
	/**
	 * Constructor.
	 * @param repository
	 * @param path
	 * @param label
	 */
	public ArrayManagedFilePath(ManagedFilePath path, String lastName) {
		this.repository = path.getManagedFileRepository();
		this.root = path.getRoot().getFirstName();

		final int nameCount = path.getNameCount();

		this.names  = new String[nameCount + 1];

		for (int i = 0 ; i < nameCount; i++){
			names[i] = path.getName(i);
		}

		names[names.length -1 ] = lastName;

		int pos = lastName.lastIndexOf('.');
		if (pos < 0){
			lastNameExtention = "";
			lastNameBase = lastName;
		} else {
			lastNameBase = lastName.substring(0, pos);
			lastNameExtention = lastName.substring(pos+1);
		}



	}

	@Override
	public String getFileNameWithoutExtension() {
		return lastNameBase;
	}

	@Override
	public int getNameCount() {
		return  names.length;
	}

	@Override
	public String getFileNameExtension() {
		return this.lastNameExtention;
	}

	@Override
	public ManagedFilePath getParent() {
		return new ArrayManagedFilePath(
				this.repository,
				this.root,
				Arrays.copyOf(this.names , this.names.length - 1)
		);
	}

	@Override
	public String getPath() {
		if (root == null){
			return "/" + StringUtils.join("/", names); // relative
		} else {
			return root + "/" + StringUtils.join("/", names); // absolute
		}
		 
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastName() {
		return names.length == 0 ? null : names[names.length-1];
	}

	public int hashCode(){
		return this.lastNameBase.hashCode();
	}

	public boolean equals(Object other){
		return (other instanceof ArrayManagedFilePath) && equalsOther( (ArrayManagedFilePath) other); 
	}

	public boolean equalsOther(ArrayManagedFilePath other){
		return this.repository.equals(other.repository) && 
		this.root.equals(other.root) &&
		Arrays.equals(names, other.names);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName(int index) {
		return names[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstName() {
		return names[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath getRoot() {
		return new ArrayManagedFilePath(repository, root );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository getManagedFileRepository() {
		return this.repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath resolve(String name) {
		String[] names = Splitter.on("/").split(name).intoArray(new String[0]);
		
		return new ArrayManagedFilePath(this.repository, this.root,  CollectionUtils.addToArray(this.names, names));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath resolve(ManagedFilePath path) {
		
		if (path == null){
			throw new IllegalArgumentException("Path is required.");
		}
		
		if (path.isAbsolute()){
			throw new IllegalArgumentException("Path is not relative");
		}
		
		final int nameCount = path.getNameCount();
		
		String[] newPath = new String[ this.names.length + nameCount];
		
		if (names.length > 0){
			System.arraycopy(this.names, 0, newPath, 0, this.names.length);
		}
		
		for (int i = 0; i < nameCount; i++){
			newPath[this.names.length + i] = path.getName(0);
		}
		
		return new ArrayManagedFilePath(this.repository, this.root,  newPath);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath resolveSibling(String name) {
		String[] names = Splitter.on("/").split(name).intoArray(new String[0]);
		
		String[] all = new String[this.names.length-1+names.length];
		
		System.arraycopy(this.names, 0, all, 0, this.names.length -1); // do not copy the last
		System.arraycopy(names, 0, all, this.names.length -1, names.length);
		
		
		return new ArrayManagedFilePath(this.repository, this.root,  all); 
	}
	
	public String toString(){
		return this.getPath();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFilePath relativize(ManagedFilePath path) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFileName() {
		return names.length > 0 ? this.names[this.names.length -1] : null; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWatchable() {
		return this.repository.isWatchable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WatchEventChannel register(WatchService watchService, Kind... events) {
		return watchService.watch(this, events);
	}
}

