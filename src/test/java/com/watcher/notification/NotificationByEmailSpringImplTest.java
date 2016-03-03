package com.watcher.notification;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import watcher.ApplicationConfig;
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan({"com.watcher"})
@ContextConfiguration(classes = ApplicationConfig.class, loader = AnnotationConfigContextLoader.class)
public class NotificationByEmailSpringImplTest {
	
	@Autowired
	private NotificationService emailApi;
	
	@Test
	public void testSendNotificationOk() {
		String toAddr = "florentina.ardean@gmail.com";
		String fromAddr = "florentina.ardean@gmail.com";
 
		// email subject
		String notificationSubject = "Hey.. This email sent by Notification Spring Test";
 
		// email body
		String notificationMessage = "You got an email. Spring Email test by Admin";
		
		//send email
		boolean isEmailSent = emailApi.sendNotification(notificationSubject, notificationMessage);
		assertTrue(isEmailSent);
	}
}
