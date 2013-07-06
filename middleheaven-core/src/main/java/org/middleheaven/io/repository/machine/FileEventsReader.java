/**
 * 
 */
package org.middleheaven.io.repository.machine;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.machine.FileIOWatchService.EventsReader;
import org.middleheaven.io.repository.watch.SimpleWatchEvent;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;

class FileEventsReader implements EventsReader{

	private ManagedFile file;
	private long lastTimeChecked = 0;
	private boolean existed;
	private long lastSize;

	public FileEventsReader (ManagedFile file){
		this.file = file;

		existed = file.exists();
		lastTimeChecked = 0;

		this.lastSize = file.getSize();
	}

	@Override
	public Collection<WatchEvent> readEvents(Set<Kind> kinds) {

		final Collection<WatchEvent> events = new LinkedList<WatchEvent>();

		if (lastTimeChecked > 0 && existed){
			if(file.exists()){
				// continues to exist. monitor change in file.

				if (file instanceof FileIOManagedFileAdapter) {
					if (((FileIOManagedFileAdapter) file).lastModified() > this.lastTimeChecked) {
						events.add(new SimpleWatchEvent(file,StandardWatchEvent.ENTRY_MODIFIED));
						
						lastTimeChecked = ((FileIOManagedFileAdapter) file).lastModified();
					}
					
					
				} else {
					if (this.lastSize != file.getSize()) {
						// modified
						if (kinds.contains(StandardWatchEvent.ENTRY_MODIFIED)){
							events.add(new SimpleWatchEvent(file ,StandardWatchEvent.ENTRY_MODIFIED));
						}

						lastTimeChecked = System.currentTimeMillis();

						this.lastSize = file.getSize();
					}
				}

				

			} else {
				// was removed
				lastTimeChecked = System.currentTimeMillis();

				if (kinds.contains(StandardWatchEvent.ENTRY_DELETED)){
					events.add(new SimpleWatchEvent(file ,StandardWatchEvent.ENTRY_DELETED));
				}
			}
		} else {
			// if didn't exist previously.
			lastTimeChecked = System.currentTimeMillis();
			
			if(file.exists()){
				existed = true;
				if (kinds.contains(StandardWatchEvent.ENTRY_CREATED)){
					// was created and creation is informed
					events.add(new SimpleWatchEvent(file ,StandardWatchEvent.ENTRY_CREATED));
				}
			}
		}

		return events;
	}

}