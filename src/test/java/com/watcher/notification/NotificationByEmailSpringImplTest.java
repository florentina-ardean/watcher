package com.watcher.notification;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import watcher.ApplicationConfigTestNotification;

public class NotificationByEmailSpringImplTest {
	@Test
	public void testSendNotificationOk() {
		ApplicationContext appContext = new AnnotationConfigApplicationContext(ApplicationConfigTestNotification.class);
		
		NotificationService emailApi = (NotificationService) appContext.getBean("notificationService");
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
