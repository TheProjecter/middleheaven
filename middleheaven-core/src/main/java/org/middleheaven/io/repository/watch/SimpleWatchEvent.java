package org.middleheaven.io.repository.watch;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.util.Hash;

/**
 * 
 */
public class SimpleWatchEvent implements WatchEvent{

	private final ManagedFile file;
	private final Kind kind;
	
	/**
	 * 
	 * Constructor.
	 * @param file the managed file.
	 */
    public SimpleWatchEvent(ManagedFile file, Kind kind){
		this.file = file;
		this.kind = kind;
	}
    
	public final ManagedFile getFile(){
		return file;
	}

	@Override
	public Kind kind() {
		return kind;
	}

	public String toString(){
		return "[" + kind + "]" + file.getPath(); 
	}
	
	@Override
	public int count() {
		return 1;
	}
	
	public int hashCode(){
		return Hash.hash(file.getPath()).hash(kind).hashCode();
	}
	
	public boolean equals(Object other){
		return (other instanceof SimpleWatchEvent) && equalsOther((SimpleWatchEvent)other); 
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(SimpleWatchEvent other) {
		return other.kind.equals(this.kind) && other.file.getPath().equals(this.file.getPath());
	}
}
