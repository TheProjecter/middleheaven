package org.middleheaven.io.repository.watch;

import java.util.concurrent.BlockingQueue;

public interface WatchEventChannel {

	/**
	 * Cancels watch service registration.
	 */
	public void close();
	
	/**
	 * The channel is valid while it is not closed.
	 * The channel can be closed by the watch service or calling close 
	 * @return
	 */
	public boolean isValid();
	
	/**
	 * The underlying watchable.
	 * @return
	 */
	public Watchable watchable();
	
	/**
	 * 
	 * @return collection of the events produced
	 */
	public BlockingQueue<WatchEvent> pollEvents() throws InterruptedException;
	
	
}
