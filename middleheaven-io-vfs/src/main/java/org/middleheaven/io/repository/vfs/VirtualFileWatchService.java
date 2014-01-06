package org.middleheaven.io.repository.vfs;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.watch.SimpleWatchEvent;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.io.repository.watch.Watchable;

/**
 * 
 */
public class VirtualFileWatchService implements WatchService {

	private BlockingQueue<VirtualFileWatchServiceWatchEventChannel> channels = new LinkedBlockingQueue<VirtualFileWatchServiceWatchEventChannel>();
	private BlockingQueue<VirtualFileWatchServiceWatchEventChannel> ready = new LinkedBlockingQueue<VirtualFileWatchServiceWatchEventChannel>();
	
	@Override
	public WatchEventChannel watch(Watchable watchable, Kind... events) {
		try {
			
			ManagedFile managedFile = fileForWatchable(watchable);
			
			FileObject fileObject = ((VirtualFileSystemManagedFile) managedFile).file;
			
			FileSystem fileSystem = fileObject.getParent().getFileSystem();
		
			VirtualFileWatchServiceWatchEventChannel wec = new VirtualFileWatchServiceWatchEventChannel(fileObject , watchable);
			
			channels.add(wec);
			
			fileSystem.addListener( fileObject , wec);
			
			return wec;
		} catch (FileSystemException e) {
			throw new VirtualFileSystemException(e);
		}
	}

	private ManagedFile fileForWatchable(Watchable watchable){
		if (watchable instanceof ManagedFilePath) {
			ManagedFilePath path = (ManagedFilePath) watchable;
			return path.getManagedFileRepository().retrive(path);
		} else if (watchable instanceof ManagedFile) {
			return (ManagedFile) watchable;
		} else {
			throw new IllegalArgumentException(watchable.getClass() + " is not an acceptable watchable for this  watch service");
		}
	}
	
	@Override
	public void close() {
		for (VirtualFileWatchServiceWatchEventChannel wec : channels){
			wec.close();
		}
	}
	
	class VirtualFileWatchServiceWatchEventChannel implements WatchEventChannel , FileListener {

		private final AtomicBoolean signaled = new AtomicBoolean(false);
		private final FileObject fileObject;
	
		private final Watchable watchable;
		private Collection<WatchEvent> events = new LinkedList<WatchEvent>();
		private boolean isValid = true;
		
		public VirtualFileWatchServiceWatchEventChannel (FileObject fileObject, Watchable watchable){
			this.fileObject = fileObject;
			this.watchable = watchable;
		}
		
		@Override
		public void close() {
			
			fileObject.getFileSystem().removeListener( fileObject , this);
			isValid = false;
		}

		@Override
		public boolean isValid() {
			return isValid;
		}

		@Override
		public Watchable watchable() {
			return watchable;
		}

		@Override
		public BlockingQueue<WatchEvent> pollEvents() {
			try {
				return new LinkedBlockingQueue<WatchEvent>(events);
			} finally {
				events = new LinkedList<WatchEvent>();
			}
		}
		
		private void addEvent(WatchEvent event){
			if (this.isValid){
				events.add(event);
				if (signaled.compareAndSet(false, true)){
					ready.add(this);
				}
			}
		}

		@Override
		public void fileChanged(org.apache.commons.vfs.FileChangeEvent event)
				throws Exception {
			addEvent(new SimpleWatchEvent(
					new VirtualFileSystemManagedFile(
							VirtualFileWatchService.this, 
							event.getFile()
					),
					StandardWatchEvent.ENTRY_MODIFIED)
			);
			
		}

		@Override
		public void fileCreated(org.apache.commons.vfs.FileChangeEvent event)
				throws Exception {
			addEvent(new SimpleWatchEvent(
					new VirtualFileSystemManagedFile(
							VirtualFileWatchService.this, 
							event.getFile()
					),
					StandardWatchEvent.ENTRY_CREATED)
			);
		}

		@Override
		public void fileDeleted(org.apache.commons.vfs.FileChangeEvent event)
				throws Exception {
			addEvent(new SimpleWatchEvent(
					new VirtualFileSystemManagedFile(
							VirtualFileWatchService.this, 
							event.getFile()
					),
					StandardWatchEvent.ENTRY_DELETED)
			);
		}
		
	}




}
