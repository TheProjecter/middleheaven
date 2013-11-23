package org.middleheaven.io.repository.watch;


public enum StandardWatchEvent implements WatchEvent.Kind {

	ENTRY_CREATED,
	ENTRY_DELETED,
	ENTRY_MODIFIED,
	OVERFLOW
}
