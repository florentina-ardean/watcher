package com.watcher.fileprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.watcher.notification.NotificationService;

@Service("fileProcessor")
public class FileProcessorImpl implements FileProcessor {
	
	@Autowired
	private NotificationService notification;
	
	@Value("${senderAddress}")
	private String senderAddress;

	@Value("${recipientAddress}")
	private String recipientAddress;
	
	@Value("${mail.subject.success}")
	private String mailSubjectSuccess;
	
	@Value("${mail.subject.success}")
	private String mailBodySuccess;
	
	@Value("${mail.subject.error}")
	private String mailSubjectError;
	
	@Value("${mail.body.error}")
	private String mailBodyError;
	
	/* (non-Javadoc)
	 * @see com.watcher.fileprocessor.FileProcessor#processFile(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean processFile(Path inputFile, Path outputFile) {
		boolean hasOperationSucceded = false;
		
		if (isZipFile(inputFile)) {
			// copy zip file to outputDirectory
			hasOperationSucceded = copyFile(inputFile, outputFile);
		} else {
			// archive file directly to output directory
			hasOperationSucceded = archiveFile(inputFile, outputFile);
		}
		
		// if file processing was good delete initial file
		if (hasOperationSucceded) {
			deleteFile(inputFile);
		}
		
		if (!hasOperationSucceded) {
			notification.sendNotification(senderAddress, recipientAddress, mailSubjectError, mailBodyError + inputFile.toString());
		} /*else {
			notification.sendNotification(senderAddress, recipientAddress, mailSubjectSuccess, mailBodySuccess);
		}*/
		
		return hasOperationSucceded;
	}

	public boolean copyFile(Path inputFile, Path outputFile) {
		boolean success = false;
		// copy sourceFile to targetFile location
		try {
			Files.copy(inputFile, outputFile, StandardCopyOption.REPLACE_EXISTING);
			success = true;
			System.out.println("Copied: " + inputFile.toString());
		} catch (InvalidPathException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return success;
	}


	/**
	 * Verify if extension is "zip"
	 */
	public boolean isZipFile(Path inputFile) {
		String fileName = inputFile.getFileName().toString();
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		return extension.equalsIgnoreCase("zip");
	}

	/**
	 * Read a file and compress it into a zip file format
	 * @return 
	 */
	public boolean archiveFile (Path inputFile, Path outputFile) {
		String fileName = inputFile.toString();
		File file = new File(fileName);
		
		String zipfileNameNoExtension = outputFile.toString().substring(0, fileName.lastIndexOf(".") + 1);
		String zipFileName = zipfileNameNoExtension + ".zip";
		
		
		boolean success = false;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		FileInputStream fis = null;

		// add a new Zip Entry to the ZipOutputStream
		ZipEntry ze = null;

		try {
			// create ZipOutputStream to write to the zip file
			fos = new FileOutputStream(zipFileName);
			zos = new ZipOutputStream(fos);

			// add a new Zip Entry to the ZipOutputStream
			ze = new ZipEntry(file.getName());
			zos.putNextEntry(ze);

			// read the file and write to ZipOutputStream
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			
			success = true;
			System.out.println(file.getCanonicalPath() + " is zipped to " + zipFileName);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the zip entry to write to zip file
				if (zos != null) {
					zos.closeEntry();
					zos.close();
				}

				if (fis != null) {
					fis.close();
				}

				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return success;

	}

	// delete file
	public void deleteFile(Path inputFile) {
		try {
			Files.delete(inputFile);
			System.out.println("Deleted: " + inputFile.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//get file path based on file name and directory
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
