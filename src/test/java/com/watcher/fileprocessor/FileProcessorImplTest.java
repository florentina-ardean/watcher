package com.watcher.fileprocessor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FileProcessorImplTest {
	private String fileName;
	private String inputDirectory = "D:/input";
	private String outputDirectory = "D:/output";

	private FileProcessorImpl fileProc = new FileProcessorImpl();

//	@Test
//	public void copyFileTestOk() {
//		fileName = "MyFile.txt";
//		boolean success = fileProc.copyFile(fileName, inputDirectory, outputDirectory);
//		assertEquals(true, success);
//	}
//
//	@Test
//	public void copyFileTestInvalidPath() {
//		fileName = "file.zip";
//		boolean success = fileProc.copyFile(fileName, inputDirectory, outputDirectory);
//		assertEquals(false, success);
//	}
}
