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
	private String senderAddress;

	@Value("${senderPassword}")
	private String senderPassword;

	@Value("${recipientAddress}")
	private String recipientAddress;

	@PostConstruct
	public void init() {
		// connect to database
		// obtain properties
		JavaMailSenderImpl ms = (JavaMailSenderImpl) mailSender;
		
		ms.setHost(host);
		ms.setPort(port);
		
		ms.setJavaMailProperties(getMailProperties());
		
		ms.setUsername(senderAddress);
		ms.setPassword(senderPassword);
		System.out.println("Spring - Mail Properties have been setup successfully.");
	}

	public boolean sendNotification(String notificationSubject, String notificationMessage) {
		boolean success = false;
		;
		SimpleMailMessage simpleEmailMessage = new SimpleMailMessage();
		simpleEmailMessage.setFrom(senderAddress);
		simpleEmailMessage.setTo(recipientAddress);
		simpleEmailMessage.setSubject(notificationSubject);
		simpleEmailMessage.setText(notificationMessage);
		mailSender.send(simpleEmailMessage);
		success = true;
		System.out.println("Spring - Mail sent successfully.");

		return success;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty(mailProtocol, mailProtocolValue);
		properties.setProperty(MAIL_SMTP_AUTH, "true");
		properties.setProperty(MAIL_SMTP_STARTTLS_ENABLE, "true");
//		properties.setProperty("mail.debug", "false");
		return properties;
	}

}
