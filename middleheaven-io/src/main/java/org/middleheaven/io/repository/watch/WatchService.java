package org.middleheaven.io.repository.watch;

import java.io.Closeable;

import org.middleheaven.io.repository.ManagedFile;

/**
 * 
 */
public interface WatchService extends Closeable  {


	/**
	 * Register a {@link ManagedFile} to be watched for changes of a given set of kinds, inside a given {@link Watchable}.
	 * @param watchable the file repository object to watch
	 * @param events kind of events to watch for.
	 * 
	 * @return a {@link WatchEventChannel} for receiving the events.
	 */
	public WatchEventChannel watch(Watchable watchable, WatchEvent.Kind ... events);

	/**
	 * Close the service.
	 * This stops all registered channels and internal monitors.
	 */
	public void close();
	
}
