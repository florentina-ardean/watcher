package com.watcher.notification;

public interface NotificationService {
	/**
	 * Send notification to predefined host  and port
	 * 
	 * @param notificationSubject
	 * @param notificationMessage
	 * @return
	 */
	boolean sendNotification(String notificationSubject, String notificationMessage);
	
	/**
	 * Send notification
	 * @param host
	 * @param port
	 * @param notificationSubject
	 * @param notificationMessage
	 * @return
	 */
	boolean sendNotification(String host, int port, String notificationSubject, String notificationMessage);

}