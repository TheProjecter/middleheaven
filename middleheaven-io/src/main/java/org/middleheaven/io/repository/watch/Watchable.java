/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository.watch;

/**
 * Interface for a file container that supports watch-dog semantics.
 *
 */
public interface Watchable {

	/**
	 * 
	 * @return <code>true</code> if the registration of listeners and event trigger are supported.
	 */
	public boolean isWatchable();
	
	/**
	 * Create a watch channel for the repository.
	 * @param events an array of events that will be monitored for.
	 * @return a {@link WatchEventChannel} to handle triggered events.
	 */
	public WatchEventChannel register(WatchService watchService, WatchEvent.Kind ... events);
	
}
