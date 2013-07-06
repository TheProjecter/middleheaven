/**
 * 
 */
package org.middleheaven.io.repository.machine;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.machine.FileIOWatchService.EventsReader;
import org.middleheaven.io.repository.watch.SimpleWatchEvent;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.util.function.Block;

class FolderEventsReader implements EventsReader{

	private ManagedFile folder;
	private long lastTimeChecked = 0;
	private Set<ManagedFile> oldFiles= new HashSet<ManagedFile>();
	private boolean existed;

	public FolderEventsReader (ManagedFile folder){

		this.folder = folder;

		existed = folder.exists();

		if (folder.exists()){
			if (!folder.getType().isFolder()){
				throw new IllegalArgumentException("managedFile must correspond with a folder");
			}

			this.folder = folder;

			for (ManagedFile file : folder.children()){
				oldFiles.add(file);
			}
			
		} 

		lastTimeChecked = System.currentTimeMillis();
	}

	public Collection<WatchEvent>  readEvents(Set<Kind> kinds){

		final Collection<WatchEvent> events = new LinkedHashSet<WatchEvent>();

		if (existed){
			if(folder.exists()){
				// continues to exist. monitor change in files.

				// the number of files can be the same but the files different

				final Set<ManagedFile> allFiles = new HashSet<ManagedFile>();
				final Set<ManagedFile> newFiles = new HashSet<ManagedFile>();

				for (ManagedFile file : folder.children()){
					newFiles.add(file);
					allFiles.add(file);
				}
				
				Collection<ManagedFile> common = CollectionUtils.intersect(newFiles, oldFiles);

				oldFiles.removeAll(common);
				newFiles.removeAll(common);

				// oldFiles contains now the deleted files.
				if (kinds.contains(StandardWatchEvent.ENTRY_DELETED)){
					for (ManagedFile file : oldFiles){
						events.add(new SimpleWatchEvent(file, StandardWatchEvent.ENTRY_DELETED));
					}
				}

				// newFiles contains now the recently added files.
				if (kinds.contains(StandardWatchEvent.ENTRY_CREATED)){
					for (ManagedFile file : newFiles){
						events.add(new SimpleWatchEvent(file, StandardWatchEvent.ENTRY_CREATED));
						if ((file instanceof FileIOManagedFileAdapter)) {
							this.lastTimeChecked = ((FileIOManagedFileAdapter) file).lastModified();
						}	
					}
				}

				// check the remaining files for modification.

				if (kinds.contains(StandardWatchEvent.ENTRY_MODIFIED)){
					for (ManagedFile file : common){
						
						if ((file instanceof FileIOManagedFileAdapter) && (((FileIOManagedFileAdapter) file).lastModified() > this.lastTimeChecked)) {
							events.add(new SimpleWatchEvent(file,StandardWatchEvent.ENTRY_MODIFIED));
							
							this.lastTimeChecked = ((FileIOManagedFileAdapter) file).lastModified();
						}	
					}

					// if events contain any event then the folder was modified
					if (!events.isEmpty()) {
						events.add(new SimpleWatchEvent(folder, StandardWatchEvent.ENTRY_MODIFIED));
					} 
				}


				oldFiles = allFiles;

			} else {
				// was removed
				if (kinds.contains(StandardWatchEvent.ENTRY_DELETED)){
					events.add(new SimpleWatchEvent(folder,StandardWatchEvent.ENTRY_DELETED));
				}
			}
		} else {
			// if didn't exist previously.

			if(folder.exists()){
				existed = true;
				
				if (kinds.contains(StandardWatchEvent.ENTRY_CREATED)){
					// was created and creation is informed
					events.add(new SimpleWatchEvent(folder,StandardWatchEvent.ENTRY_CREATED));

				}
				
				// iterate all files 

				for (ManagedFile file : folder.children()){
					events.add(new SimpleWatchEvent(file ,StandardWatchEvent.ENTRY_CREATED));
				}

			}
		}

		return events;
	}

}