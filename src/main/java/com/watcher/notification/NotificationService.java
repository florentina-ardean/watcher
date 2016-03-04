package com.watcher.notification;

public interface NotificationService {

	boolean sendNotification(String fromAddress, String toAddress, String notificationSubject,
			String notificationMessage);
}