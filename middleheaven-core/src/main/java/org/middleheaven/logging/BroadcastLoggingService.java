/**
 * 
 */
package org.middleheaven.logging;

import org.middleheaven.core.annotations.Service;
import org.middleheaven.events.EventListenersSet;

/**
 * {@link Service} that allows for logging broadcast.
 */
public class BroadcastLoggingService implements LoggingService {

	private EventListenersSet<LoggingEventListener> listeners = EventListenersSet.newSet(LoggingEventListener.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(LoggingEvent event) {
		listeners.broadcastEvent().onLoggingEvent(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLogListener(LoggingEventListener listener) {
		listeners.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLogListener(LoggingEventListener listener) {
		listeners.removeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllListeners() {
		listeners.removeAll();
	}

}
