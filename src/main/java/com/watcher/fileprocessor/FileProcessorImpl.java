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

import com.watcher.notification.NotificationByEmailImpl;
import com.watcher.notification.NotificationService;

public class FileProcessorImpl {

	private NotificationService notification = new NotificationByEmailImpl();

	public void processFile(String fileName, String inputDirectory, String outputDirectory) {
		boolean hasOperationSucceded = false;
		
		if (isZipFile(fileName)) {
			// copy zip file to outputDirectory
			hasOperationSucceded = copyFile(fileName, inputDirectory, outputDirectory);
		} else {
			// archive file directly to output directory
			hasOperationSucceded = archiveFile(fileName, inputDirectory, outputDirectory);
		}
		
		// if no exception occurs delete initial file
		if (hasOperationSucceded) {
			deleteFile(fileName, inputDirectory);
		}
		
		if (!hasOperationSucceded) {
			notification.sendNotification("Process file" + fileName);
		}
	}

	private boolean copyFile(String fileName, String inputDir, String outputDir) {
		boolean success = false;
		Path sourceFile = null;
		Path targetFile = null;

		// copy sourceFile to targetFile location
		try {
			sourceFile = Paths.get(inputDir, fileName);
			targetFile = Paths.get(outputDir, fileName);

			Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
			
			success = true;
			
			System.out.println("Copied: " + sourceFile.toString());
			
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
	private boolean isZipFile(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		return extension.equals("zip");
	}

	/**
	 * Read a file and compress it into a zip file format
	 * @return 
	 */
	private boolean archiveFile(String fileName, String inputDirectory, String outputDirectory) {
		File file = new File(inputDirectory + "/" + fileName);
		String fileNameNoExtension = fileName.substring(0, fileName.lastIndexOf("."));
		String zipFileName = outputDirectory + "/" + fileNameNoExtension + ".zip";
		
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
	private void deleteFile(String fileName, String directory) {
		try {
			Path file = Paths.get(directory, fileName);
			Files.delete(file);
			System.out.println("Deleted: " + file.toString());
		} catch (InvalidPathException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
