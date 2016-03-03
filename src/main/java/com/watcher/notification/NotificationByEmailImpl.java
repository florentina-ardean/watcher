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

import org.springframework.beans.factory.annotation.Value;

public class NotificationByEmailImpl implements NotificationService {
	private Properties mailServerProperties;
	private Session mailSession;
	private MimeMessage mailMessage;

	@Value("${mailProtocol}")
	private String mailProtocol;
	
	@Value("${host}")
	private String host;
	
	@Value("${mail_smtp_port}")
	private String MAIL_SMTP_PORT;
	
	@Value("${mail_smtp_port_number}")
	private String MAIL_SMTP_PORT_NUMBER;
	
	@Value("${mail_smtp_auth}")
	private String MAIL_SMTP_AUTH;
	
	@Value("${mail_smtp_starttls_enable}")
	private String MAIL_SMTP_STARTTLS_ENABLE;

	
	@Value("${senderAddress}")
	private String senderAddress = "florentina.ardean@gmail.com";
	
	@Value("${senderPassword}")
	private String senderPassword = "Gandirepozitiva01";
	
	@Value("${recipientAddress}")
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
			mailSession = Session.getDefaultInstance(mailServerProperties, null);

			// and generate mail message
			mailMessage = generateMailMessage(mailSession, notificationMessage);

			// send mail
			sendEmail(mailSession);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	private void sendEmail(Session mailSession) {
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
		mailServerProperties.put(MAIL_SMTP_AUTH, "true");
		mailServerProperties.put(MAIL_SMTP_STARTTLS_ENABLE, "true");
		
		System.out.println("Mail Server Properties have been setup successfully.");
	}

}
