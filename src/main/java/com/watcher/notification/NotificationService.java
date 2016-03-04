package com.watcher.notification;

public interface NotificationService {

	//boolean sendNotification(String notificationSubject, String notificationMessage);

	boolean sendNotification(String fromAddress, String toAddress, String notificationSubject,
			String notificationMessage);

	//boolean sendNotification(String host, int port, String notificationSubject, String notificationMessage);
}