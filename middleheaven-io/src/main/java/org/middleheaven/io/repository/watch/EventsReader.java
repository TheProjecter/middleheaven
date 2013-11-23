/**
 * 
 */
package org.middleheaven.io.repository.watch;

import java.util.Collection;
import java.util.Set;

import org.middleheaven.io.repository.watch.WatchEvent.Kind;

public interface EventsReader {
	
	public Collection<WatchEvent>  readEvents(Set<Kind> kinds);
}