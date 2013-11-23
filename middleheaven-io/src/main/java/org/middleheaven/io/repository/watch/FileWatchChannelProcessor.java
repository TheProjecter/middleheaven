package org.middleheaven.io.repository.watch;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.imageio.spi.ServiceRegistry;


/**
 * Handles {@link WatchEventChannel} events using a given {@link FileChangeStrategy}. 
 */
public class FileWatchChannelProcessor implements Closeable {


	private final Set<WatchEventChannel> channels = new CopyOnWriteArraySet<WatchEventChannel>();

	private final FileChangeStrategy stategy;

	private final Collection<ProcessorThread> processors = new ArrayList<ProcessorThread>();

	public FileWatchChannelProcessor(FileChangeStrategy fileChangeStratey){
		this.stategy = fileChangeStratey;
		
//		ServiceRegistry.getService(BootstrapService.class).addListener(new BootstapListener(){
//
//			@Override
//			public void onBoostapEvent(BootstrapEvent event) {
//				if (event.isBootdown()){
//					close();
//				}
//			}
//
//		});
	}	
	
	public synchronized void close() {
		for (ProcessorThread p : this.processors){
			p.interrupt();
		}
	}

	public void add(WatchEventChannel channel){
		channels.add(channel);
	}


	public void start (){
		for (Iterator<WatchEventChannel> it = channels.iterator(); it.hasNext();){
			ProcessorThread processorThread = new ProcessorThread(it.next());
			
			processors.add(processorThread);
			processorThread.start();
		}
	}

	private class ProcessorThread extends Thread {

		private WatchEventChannel channel;

		ProcessorThread (WatchEventChannel channel){
			this.setDaemon(true);
			this.setName("Processing Thread for watchable " + channel.watchable());
			this.channel = channel;
		}

		public void run (){
			while (true) {
				try {
					if (channel.isValid()) {
						
						WatchEvent event = channel.pollEvents().take();
						
						stategy.onChange(event);
					} else {
						break;
					}
				} catch (InterruptedException e) {
					break;
				}

			}

		}
	}

}