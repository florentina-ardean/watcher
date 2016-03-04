package com.watcher.notification;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service("notificationService")
public class NotificationByEmailSpringImpl implements NotificationService {

	@Autowired
	private MailSender mailSender; // MailSender interface defines a strategy
									// for sending simple mails
	// private SimpleMailMessage simpleEmailMessage;

	@Value("${host}")
	private String host;

	@Value("${mailProtocol}")
	private String mailProtocol;

	@Value("${mailProtocolValue}")
	private String mailProtocolValue;

	@Value("${mail_smtp_port_number}")
	private int port;

	@Value("${mail_smtp_auth}")
	private String MAIL_SMTP_AUTH;

	@Value("${mail_smtp_starttls_enable}")
	private String MAIL_SMTP_STARTTLS_ENABLE;

	@Value("${senderAddress}")
	private String mailUsername;

	@Value("${senderPassword}")
	private String mailPassword;

	@PostConstruct
	public void init() {
		// connect and obtain properties
		setMailSender(host, port, mailUsername, mailPassword);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.watcher.notification.NotificationService#sendNotification(java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendNotification(String fromAddress, String toAddress, String notificationSubject,
			String notificationMessage) {
		boolean success = false;

		SimpleMailMessage simpleEmailMessage = new SimpleMailMessage();

		if (toAddress != null && notificationMessage != null) {
			setEmailMessage(simpleEmailMessage, fromAddress, toAddress, notificationSubject, notificationMessage);
			mailSender.send(simpleEmailMessage);
			success = true;
			System.out.println("Spring - Mail sent successfully.");
		} else if (toAddress == null) {
			success = false;
			System.out.println("Spring - Mail not send receiver address is null.");
		} else if (notificationMessage == null) {
			success = false;
			System.out.println("Spring - Mail not send notification message is null.");
		}

		return success;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty(mailProtocol, mailProtocolValue);
		properties.setProperty(MAIL_SMTP_AUTH, "true");
		properties.setProperty(MAIL_SMTP_STARTTLS_ENABLE, "true");
		return properties;
	}

	public void setMailSender(String connectHost, int connectPort, String username, String password) {
		JavaMailSenderImpl ms = (JavaMailSenderImpl) mailSender;
		ms.setHost(connectHost);
		ms.setPort(connectPort);
		ms.setUsername(username);
		ms.setPassword(password);
		ms.setJavaMailProperties(getMailProperties());
	}

	public void setMailSender(String connectHost, int connectPort) {
		setMailSender(connectHost, connectPort, mailUsername, mailPassword);
	}

	public void setEmailMessage(SimpleMailMessage emailMessage, String fromAddress, String toAddress,
			String emailSubject, String emailText) {
		emailMessage.setFrom(fromAddress);
		emailMessage.setTo(toAddress);
		emailMessage.setSubject(emailSubject);
		emailMessage.setText(emailText);
	}
}
