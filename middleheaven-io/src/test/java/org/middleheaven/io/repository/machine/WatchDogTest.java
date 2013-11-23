package org.middleheaven.io.repository.machine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.watch.FileChangeStrategy;
import org.middleheaven.io.repository.watch.FileIOWatchService;
import org.middleheaven.io.repository.watch.FileWatchChannelProcessor;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEventChannel;

/**
 * 
 */
public class WatchDogTest {

	@Test
	public void testFileWatchDog () throws InterruptedException, IOException{
		
		
		File f  = new File(".");
		
		FileIOStrategy strategy = new FileIOStrategy();
		
		final ManagedFileRepository openFileRepository = strategy.openFileRepository(f.toURI());
		
		ManagedFile file = openFileRepository.retrive(openFileRepository.getRootPaths().iterator().next());
		
		FileIOWatchService dogService = (FileIOWatchService) strategy.getWatchService();
		
		dogService.setPeriod(2000);
		
		WatchEventChannel channel = file.register(dogService, StandardWatchEvent.ENTRY_CREATED, StandardWatchEvent.ENTRY_DELETED, StandardWatchEvent.ENTRY_MODIFIED);
		
		final List<WatchEvent> events = new ArrayList<WatchEvent>(4);
		
		FileWatchChannelProcessor processor = new FileWatchChannelProcessor(new FileChangeStrategy() {
			
			@Override
			public void onChange(WatchEvent event) {
				events.add(event);
			}
		});
		
		processor.add(channel);
		processor.start();
		
		File n = new File(f, "test.txt");
		
		n.createNewFile(); // ENTRY_CREATED (file) , ENTRY_MODIFIED (folder)

		Thread.currentThread().sleep(5000);
		
		n.delete(); // ENTRY_DELETED (file) , ENTRY_MODIFIED (folder)
		
		Thread.currentThread().sleep(5000);
		assertEquals(4 , events.size());


		assertEquals(StandardWatchEvent.ENTRY_CREATED , events.get(0).kind());
		assertTrue(events.get(0).getFile().getType().isVirtual()); // has it was deleted already
		assertEquals(StandardWatchEvent.ENTRY_MODIFIED , events.get(1).kind());
		assertTrue(events.get(1).getFile().getType().isFolder());
		assertEquals(StandardWatchEvent.ENTRY_DELETED , events.get(2).kind());
		assertTrue(events.get(2).getFile().getType().isVirtual()); // has it was deleted already
		assertEquals(StandardWatchEvent.ENTRY_MODIFIED , events.get(3).kind());
		assertTrue(events.get(3).getFile().getType().isFolder());
	}
	
}
