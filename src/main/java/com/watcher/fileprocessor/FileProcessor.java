package com.watcher.fileprocessor;

import java.nio.file.Path;

public interface FileProcessor {

	boolean processFile(Path inputFile, Path outputFile);
	Path getFilePath(String fileName, String directory);

}