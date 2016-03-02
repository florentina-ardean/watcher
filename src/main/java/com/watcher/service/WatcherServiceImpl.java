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
import java.util.concurrent.TimeUnit;

import com.watcher.fileprocessor.FileProcessorImpl;

public class WatcherServiceImpl implements WatcherService {
	FileProcessorImpl fileProcessor = new FileProcessorImpl();

	private final String INPUT_FOLDER = "D:/input";
	private final String OUTPUT_FOLDER = "D:/output";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.watcher.service.WatcherService#start()
	 */
	public void start() {
		Path myDir = null;
		try {
			// define a folder root
			myDir = Paths.get(INPUT_FOLDER);

		} catch (InvalidPathException ipe) {
			System.out.println("InvalidPathException: " + ipe.toString());
		}

		// creating watch service and register for events
		try (WatchService watcher = myDir.getFileSystem().newWatchService()) {

			myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

			for (;;) {

				WatchKey watckKey = watcher.poll(10, TimeUnit.SECONDS);

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

							// process file
							fileProcessor.processFile(fileName, INPUT_FOLDER, OUTPUT_FOLDER);
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
