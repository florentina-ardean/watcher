package com.watcher.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import watcher.ApplicationConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan({ "com.watcher" })
@ContextConfiguration(classes = ApplicationConfig.class, loader = AnnotationConfigContextLoader.class)
public class NotificationByEmailSpringImplTest {

	@Autowired
	private NotificationService emailSender;

	private GreenMail testSmtp;
	
	@Value("${senderAddress}")
	private String senderAddress;

	@Value("${recipientAddress}")
	private String recipientAddress;
	
	@Value("${mail.subject.generic}")
	private String mailSubject;
	
	@Value("${mail.body.generic}")
	private String mailBody;

	@Before
	public void testSmtpInit() {
		testSmtp = new GreenMail(ServerSetupTest.SMTP);
		testSmtp.start();
		
		((NotificationByEmailSpringImpl)emailSender).setMailSender("localhost", 3025);
	}

	@Test
	public void testSendNotificationOk() throws MessagingException {
		// email subject
		String notificationSubject = "This email sent by Notification Spring Test";

		// email body
		String notificationMessage = "You got an email. Spring Email test by Admin";

		// send email
		boolean isEmailSent = emailSender.sendNotification(senderAddress, recipientAddress, notificationSubject, notificationMessage);

		assertTrue(isEmailSent);
	}
	
	@Test
	public void testSendNotificationUseSMTPServer4Params() throws MessagingException {
		emailSender.sendNotification(senderAddress, recipientAddress, mailSubject, mailBody);
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(1, messages.length);
	}

	@Test
	public void testSendNotificationUseSMTPServer() throws MessagingException {
		// email subject
		String notificationSubject = "This email sent by Notification Spring Test";

		// email body
		String notificationMessage = "You got an email. Spring Email test by Admin";

		// send email
		emailSender.sendNotification(senderAddress, recipientAddress, notificationSubject, notificationMessage);

		Message[] messages = testSmtp.getReceivedMessages();

		assertEquals(1, messages.length);

		assertEquals(notificationSubject, messages[0].getSubject());

		String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
		assertEquals(notificationMessage, body);
	}

	

	@After
	public void cleanup() {
		testSmtp.stop();
	}
}
