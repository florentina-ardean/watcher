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

		// set host and port number for smtp server
		((NotificationByEmailSpringImpl) emailSender).setMailSender("localhost", 3025);
	}

	@Test
	public void testSendNotificationOk() {
		String notificationSubject = "This email sent by Notification Spring Test";
		String notificationMessage = "You got an email. Spring Email test by Admin";
		boolean isEmailSent = emailSender.sendNotification(senderAddress, recipientAddress, notificationSubject,
				notificationMessage);
		assertTrue(isEmailSent);
	}

	@Test
	public void testSendNotificationUseSMTPServer4Params() {
		boolean isEmailSent = emailSender.sendNotification(senderAddress, recipientAddress, mailSubject, mailBody);
		// check messages with GreenMail server
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(1, messages.length);
		assertTrue(isEmailSent);
	}

	@Test
	public void testSendNotificationUseSMTPServer() throws MessagingException {
		boolean isEmailSent = emailSender.sendNotification(senderAddress, recipientAddress, mailSubject, mailBody);

		// check messages with GreenMail server
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(1, messages.length);
		assertEquals(mailSubject, messages[0].getSubject());
		String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
		assertEquals(mailBody, body);
		assertTrue(isEmailSent);
	}

	@Test
	public void testSendNotificationUseSMTPServerNullSender() {
		boolean isEmailSent = emailSender.sendNotification(null, recipientAddress, mailSubject, mailBody);
		assertTrue(isEmailSent);
		
		// check messages with GreenMail server
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(1, messages.length);
	}
	
	@Test
	public void testSendNotificationUseSMTPServerNullRecipient() {
		boolean isEmailSent = emailSender.sendNotification(senderAddress, null, mailSubject, mailBody);
		assertEquals(false, isEmailSent);
		
		// check messages with GreenMail server
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(0, messages.length);
	}
	
	
	@Test
	public void testSendNotificationUseSMTPServerNullSubject() {
		boolean isEmailSent = emailSender.sendNotification(senderAddress, recipientAddress, null, mailBody);
		assertTrue(isEmailSent);
		
		// check messages with GreenMail server
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(1, messages.length);

	}
	
	@Test
	public void testSendNotificationUseSMTPServerNullBody() {
		boolean isEmailSent = emailSender.sendNotification(senderAddress, recipientAddress, mailSubject, null);
		assertEquals(false, isEmailSent);
		
		// check messages with GreenMail server
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(0, messages.length);
	}

	@After
	public void cleanup() {
		testSmtp.stop();
	}
}
