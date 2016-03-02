package com.watcher.notification;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class NotificationByEmailImpl implements NotificationService {
	private Properties mailServerProperties;
	private Session mailSession;
	private MimeMessage mailMessage;

	private String mailProtocol = "smtp";
	private String host = "smtp.gmail.com";
	
	private final String MAIL_SMTP_PORT = "mail.smtp.port";
	private final String MAIL_SMTP_PORT_NUMBER = "587";
	
	private final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	private final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	private final String TRUE = "true";
	
	private String senderAddress = "florentina.ardean@gmail.com";
	private String senderPassword = "Gandirepozitiva01";
	
	private String recipientAddress = "florentina.ardean@gmail.com";

	/* (non-Javadoc)
	 * @see com.watcher.notification.NotificationService#sendNotification(java.lang.String)
	 */
	public void sendNotification(String notificationMessage) {

		try {
			// Set Mail Server Properties.
			mailServerProperties = System.getProperties();
			setMailServerProperties(mailServerProperties);

			// Get mail session
			System.out.println("Get Mail Session.");
			mailSession = Session.getDefaultInstance(mailServerProperties, null);

			// and generate mail message
			mailMessage = generateMailMessage(mailSession, notificationMessage);

			// send mail
			sendMail(mailSession);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMail(Session mailSession) {
		Transport transport = null;
		try {
			transport = mailSession.getTransport(mailProtocol);

			// Enter your correct gmail UserID and Password
			transport.connect(host, senderAddress, senderPassword);
			transport.sendMessage(mailMessage, mailMessage.getRecipients(Message.RecipientType.TO));

			System.out.println("Mail sent successfully.");
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} finally {
			try {
				transport.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}

	}

	private MimeMessage generateMailMessage(Session mailSession, String notificationMessage) {
		MimeMessage mailMessage = new MimeMessage(mailSession);

		try {
			InternetAddress adminInternetAddress = new InternetAddress(recipientAddress);
			mailMessage.addRecipient(Message.RecipientType.TO, adminInternetAddress);
			mailMessage.setSubject("Important Notification!!!");

			String emailBody = notificationMessage + "<br><br> Watcher application";
			mailMessage.setContent(emailBody, "text/html");

			System.out.println("Mail Session has been created successfully.");

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return mailMessage;
	}

	private void setMailServerProperties(Properties mailServerProperties) {
		mailServerProperties.put(MAIL_SMTP_PORT, MAIL_SMTP_PORT_NUMBER);
		
		mailServerProperties.put(MAIL_SMTP_AUTH, TRUE);
		
		mailServerProperties.put(MAIL_SMTP_STARTTLS_ENABLE, TRUE);

		System.out.println("Mail Server Properties have been setup successfully.");
	}

}
