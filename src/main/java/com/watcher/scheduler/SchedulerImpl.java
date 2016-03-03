package com.watcher.scheduler;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.watcher.fileprocessor.FileProcessor;

public class SchedulerImpl implements Scheduler {
	@Value("${inputfolder}")
	public String inputFolder;
	
	@Value("${outputfolder}")
	public String outputFolder ;
	
	@Value("${timeToCheckNewFiles}")
	public long timeToCheckNewFiles ;
	
	@Autowired
	FileProcessor fileProcessor;
	
	/* (non-Javadoc)
	 * @see com.watcher.scheduler.Scheduler#execute(java.util.concurrent.CopyOnWriteArrayList, int)
	 */
	public void execute (final CopyOnWriteArrayList<String> filesToBeProcessed) {
		
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {

				for (int i = filesToBeProcessed.size() - 1; i >= 0; i--) {
					Path inputFile = getFilePath(filesToBeProcessed.get(i), inputFolder);
					Path outputFile = getFilePath(filesToBeProcessed.get(i), outputFolder);
					boolean success = fileProcessor.processFile(inputFile,outputFile);
					if (success) {
						filesToBeProcessed.remove(i);
					}
				}
			}
		}, 0, timeToCheckNewFiles, TimeUnit.SECONDS);

	}
	
	public Path getFilePath(String fileName, String directory) {
		Path file = null;
		
		try {
			file = Paths.get(directory, fileName);
		} catch(InvalidPathException e) {
			e.printStackTrace();
		}
		return file;
	}
}
