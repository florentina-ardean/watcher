package watcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.watcher.service.WatcherServiceImpl;

public class Application_old {
	private static final String INPUT_FOLDER = "D:/input";
	private static final String OUTPUT_FOLDER = "D:/output";

	public static void main(String[] args) {
		startWatcher();
	}
	
	public static void startWatcher(){
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
							processFile(fileName);
						}
					}
					// Reset the key
					boolean isWatchKeyValid = watckKey.reset();
					if (!isWatchKeyValid) {
						break;
					}
				}
			}
		} catch (IOException ioe) {
			System.out.println("IOException main: " + ioe.toString());
		} catch (InterruptedException e) {
			System.out.println("InterruptedException: " + e.toString());
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}
	public static void processFile(String fileName) {
		System.out.println("processFile");
		// extract file extension
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

		// check if archive is correct - not working

		if (!extension.equals("zip")) {
			// archive file
			archiveFile(fileName, OUTPUT_FOLDER);
		} else {
			// copy file fileName from INPUT_FOLDER to OUTPUT_FOLDER
			copyFile(fileName, INPUT_FOLDER, OUTPUT_FOLDER);
		}

		// delete original file
		deleteFile(fileName, INPUT_FOLDER);

		// email for corrupted file
		sendEmail();
	}

	public static void sendEmail() {
		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage generateMailMessage;
		
		try {
			// Step1
			System.out.println("Setup Mail Server Properties..");
			mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.port", "587");
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
			System.out.println("Mail Server Properties have been setup successfully..");
	
			// Step2
			System.out.println("Get Mail Session..");
			getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			generateMailMessage = new MimeMessage(getMailSession);
			InternetAddress adminInternetAddress = new InternetAddress("florentina.ardean@gmail.com");
			generateMailMessage.addRecipient(Message.RecipientType.TO, adminInternetAddress);
			generateMailMessage.setSubject("Greetings from Crunchify..");
			String emailBody = "Test email by Crunchify.com JavaMail API example. "
					+ "<br><br> Regards, <br>Crunchify Admin";
			generateMailMessage.setContent(emailBody, "text/html");
			System.out.println("Mail Session has been created successfully..");
	
			// Step3
			System.out.println("Get Session and Send mail.");
			Transport transport = getMailSession.getTransport("smtp");
	
			// Enter your correct gmail UserID and Password
			// if you have 2FA enabled then provide App Specific Password
			transport.connect("smtp.gmail.com", "florentina.ardean@gmail.com", "Gandirepozitiva01");
			transport.sendMessage(generateMailMessage, generateMailMessage.getRecipients(Message.RecipientType.TO));
			transport.close();
			
			System.out.println("Mail sent successfully.");
		} catch(MessagingException me) {
			me.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Not working
	public static void sendEmailNotWorking() {
		// Recipient's email ID needs to be mentioned.
		String to = "florentina.ardean@gmail.com";

		// Sender's email ID needs to be mentioned
		String from = "florentina.herea@gmail.com";

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("This is A test email!");

			// Now set the actual message
			message.setText("This is actual message test");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	public static void copyFile(String fileName, String inputDir, String outputDir) {
		Path sourceFile = null;
		Path targetFile = null;

		try {
			sourceFile = Paths.get(inputDir, fileName);
			targetFile = Paths.get(outputDir, fileName);
		} catch (InvalidPathException ipe) {
			System.out.println("InvalidPathException: " + ipe.toString());
		}

		// copy sourceFile to targetFile location
		try {
			Files.copy(sourceFile, targetFile, StandardCopyOption.COPY_ATTRIBUTES);
			System.out.println("Copied: " + sourceFile.toString());
		} catch (IOException e) {
			System.out.println("IOException copyFile: " + e.toString());
		}
	}

	// Read a file “C:\\spy.log” and
	// compress it into a zip file – “C:\\MyFile.zip“.
	public static void archiveFile(String fileName, String directory) {
		System.out.println("archiveFile");

		File file = new File(directory + "/" + fileName);
		String fileNameNoExtension = fileName.substring(0, fileName.length() - 4);
		String zipFileName = directory + "/" + fileNameNoExtension + ".zip";

		zipSingleFile(file, zipFileName);
	}

	private static void zipSingleFile(File file, String zipFileName) {
		try {
			// create ZipOutputStream to write to the zip file
			FileOutputStream fos = new FileOutputStream(zipFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);

			// add a new Zip Entry to the ZipOutputStream
			ZipEntry ze = new ZipEntry(file.getName());
			zos.putNextEntry(ze);

			// read the file and write to ZipOutputStream
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			// Close the zip entry to write to zip file
			zos.closeEntry();

			// Close resources
			zos.close();
			fis.close();
			fos.close();

			System.out.println(file.getCanonicalPath() + " is zipped to " + zipFileName);

		} catch (IOException e) {
			System.out.println("IOException zipSingleFile: " + e.toString());
		}

	}

	// delete file
	public static void deleteFile(String fileName, String directory) {
		System.out.println("deleteFile");
		Path file = null;
		try {
			file = Paths.get(directory, fileName);
			Files.delete(file);
			System.out.println("Deleted: " + file.toString());
		} catch (InvalidPathException ipe) {
			System.out.println("InvalidPathException: " + ipe.toString());
		} catch (IOException e) {
			System.out.println("IOException deleteFile: " + e.toString());
		}
	}

	/**
	 * Determine whether a file is a ZIP File.
	 */
	public static boolean isZipFile(String fileName, String directory) {
		byte[] MAGIC = { 'P', 'K', 0x3, 0x4 };
		byte[] buffer = new byte[MAGIC.length];
		boolean isZip = true;

		String filePath = directory + "/" + fileName;
		File file = null;
		long test = 0;
		try {
			file = new File(filePath);
			if (file.isDirectory()) {
				return false;
			}

			if (file.length() < 4) {
				return false;
			}

			// DataInputStream in = new DataInputStream(new
			// BufferedInputStream(new FileInputStream(file)));
			// test = in.readInt();
			// in.close();

			RandomAccessFile raf = new RandomAccessFile(filePath, "r");
			raf.readFully(buffer);
			for (int i = 0; i < MAGIC.length; i++) {
				if (buffer[i] != MAGIC[i]) {
					isZip = false;
					break;
				}
			}
			// test = raf.readInt();
			raf.close();

		} catch (FileNotFoundException fnf) {
			System.out.println("FileNotFoundException isZipFile: " + fnf.toString());
		} catch (IOException ioe) {
			System.out.println("IOException isZipFile: " + ioe.toString());
		}
		// return test == 0x504b0304;
		return isZip;
	}

}
