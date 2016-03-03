package com.watcher.fileprocessor;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import watcher.ApplicationConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan({ "com.watcher" })
@ContextConfiguration(classes = ApplicationConfig.class, loader = AnnotationConfigContextLoader.class)
public class FileProcessorImplTest {
	
	@Value("${inputfolder}")
	public String inputFolder;

	@Value("${outputfolder}")
	public String outputFolder;

	@Autowired
	private FileProcessor fileProcessor;

	String fileName;
	String zipFileName;
	
	Path inputFile;
	Path outputFile ;

	@Before
	public void init() {
		zipFileName = "MyFile.zip";
		outputFile = fileProcessor.getFilePath(zipFileName, outputFolder);
	}
	
	public void createfile(String fileName) {
		inputFile = fileProcessor.getFilePath(fileName, inputFolder);
		try {
			Files.deleteIfExists(inputFile);
			Files.createFile(inputFile);
		} catch (IOException ex) {
			System.out.println("Error creating file" + inputFile.toString());
		}
	}

	@Test
	public void processFileTestOk() {
		fileName = "MyFile.txt";
		createfile(fileName);
		boolean success = fileProcessor.processFile(inputFile, outputFile);
		assertTrue(success);
	}

	@Test
	public void processFileTestCheckOutputDirectory() {
		fileName = "MyFile.txt";
		createfile(fileName);
		
		fileProcessor.processFile(inputFile, outputFile);
		boolean pathExists = Files.exists(outputFile, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
		assertTrue(pathExists);
	}
	
	@Test
	public void processZipFileTestCheckOutputDirectory() {
		fileName = "MyFile.zip";
		createfile(fileName);
		
		fileProcessor.processFile(inputFile, outputFile);
		boolean pathExists = Files.exists(outputFile, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
		assertTrue(pathExists);
	}

	@After
	public void tearDown() {
		try {
			Files.deleteIfExists(outputFile);
		} catch (IOException ex) {
			System.out.println("Error creating file" + outputFile.toString());
		}
	}
}
