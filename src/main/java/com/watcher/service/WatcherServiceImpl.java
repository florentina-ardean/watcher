package com.watcher.service;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Value;

public class WatcherServiceImpl implements WatcherService {
	@Value("${inputfolder}")
	public String inputFolder;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.watcher.service.WatcherService#start()
	 */
	/* (non-Javadoc)
	 * @see com.watcher.service.WatcherService#start(java.util.concurrent.CopyOnWriteArrayList)
	 */
	public void start(CopyOnWriteArrayList<String> filesToBeProcess) {
		Path myDir = null;
		try {
			// define a folder root
			myDir = Paths.get(inputFolder);

		} catch (InvalidPathException ipe) {
			System.out.println("InvalidPathException: " + ipe.toString());
		}
		
		// creating watch service and register for events
		try (WatchService watcher = myDir.getFileSystem().newWatchService()) {

			myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

			for (;;) {

				WatchKey watckKey = watcher.poll();

				try { Thread.sleep(100); } catch (Exception ex) {}
				
				// process events
				if (watckKey != null) { // while (watckKey != null)
					List<WatchEvent<?>> events = watckKey.pollEvents();
					for (WatchEvent event : events) {
						if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
							continue;
						}

						if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
							String fileName = event.context().toString();
							System.out.println("Created: " + fileName);

							// add fileName to list to be process later
							filesToBeProcess.add(fileName);
						}
					}
					// Reset the key
					boolean isWatchKeyValid = watckKey.reset();
					if (!isWatchKeyValid) {
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
