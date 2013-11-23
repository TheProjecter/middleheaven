package org.middleheaven.io.repository.watch;

import org.middleheaven.io.repository.ManagedFile;

public interface WatchEvent {

	
	interface Kind {
		
		String name();
	}
	
	
	public ManagedFile getFile();
	
	public Kind kind();

	public int count();
}
