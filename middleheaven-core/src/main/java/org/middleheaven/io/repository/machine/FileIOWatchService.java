package org.middleheaven.io.repository.machine;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.io.repository.watch.Watchable;


/**
 * Watches a folder for updates.
 * The listeners are informed if changes are made to the folder content, namely :
 * <li>
 * <ul>The number of files changes</ul>
 * </li>
 */
public class FileIOWatchService implements WatchService {

	private final static int SIX_SECONDS = 6000;

	private Thread monitorThread;

	private long period = SIX_SECONDS;

	private final List<LegacyStrategyWatchEventChannel> channels = new CopyOnWriteArrayList<LegacyStrategyWatchEventChannel>();

	/**
	 * 
	 * Constructor.
	 * @param folder
	 */
	public FileIOWatchService (){
		monitorThread = new Thread(new Dog());
		monitorThread.setDaemon(true);
		monitorThread.setName("FileWatching dog");
		
		ServiceRegistry.getService(BootstrapService.class).addListener(new BootstapListener(){

			@Override
			public void onBoostapEvent(BootstrapEvent event) {
				if (event.isBootdown()){
					close();
				}
			}
			
		});
	}

	public interface EventsReader {
		public Collection<WatchEvent>  readEvents(Set<Kind> kinds);
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public void start(){
		if (!monitorThread.isAlive()){
			monitorThread.start();
		}
	}


	private class Dog implements Runnable{

		@Override
		public void run() {
			while(true) {
				try {

					for (LegacyStrategyWatchEventChannel w : channels){
						w.update();
					}

					Thread.currentThread().sleep(period);

				} catch(InterruptedException e) {
					break;
				}
			}

			channels.clear();
		}

	}


	@Override
	public WatchEventChannel watch(Watchable watchable, Kind... events) {

		ManagedFile managedFile = fileForWatchable(watchable);

		EventsReader reader;


		if (managedFile.getType().isFile()) {
			reader = new FileEventsReader(managedFile);
		} else {
			reader = new FolderEventsReader(managedFile);
		}

		LegacyStrategyWatchEventChannel c = new LegacyStrategyWatchEventChannel(watchable , new HashSet<Kind>(Arrays.asList(events)) , reader);

		this.channels.add(c);

		return c;
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

		monitorThread.interrupt();

	}


	private class LegacyStrategyWatchEventChannel implements WatchEventChannel {

		private Watchable watchable;
		private boolean closed = false;
		private BlockingQueue<WatchEvent> events = new LinkedBlockingQueue<WatchEvent>();
		private Set<Kind> kinds;
		private EventsReader reader;

		public LegacyStrategyWatchEventChannel(Watchable watchable, Set<Kind> kinds, EventsReader reader){
			this.watchable = watchable;
			this.kinds = kinds;
			this.reader = reader;
		}

		@Override
		public synchronized void close() {
			this.closed = true;
			channels.remove(this);
		}

		@Override
		public boolean isValid() {
			return !closed;
		}

		@Override
		public Watchable watchable() {
			return watchable;
		}

		@Override
		public BlockingQueue<WatchEvent> pollEvents() {
			return events;
		}

		public void update(){

			if (this.kinds.isEmpty()){
				return;
			}

			if (!closed){
				// only update after the previous events where consumed

				Collection<WatchEvent> newEvents = reader.readEvents(this.kinds);

				events.addAll(newEvents);

			}
		}


	}

}
