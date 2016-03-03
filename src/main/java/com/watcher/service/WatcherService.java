package com.watcher.service;

import java.util.concurrent.CopyOnWriteArrayList;

public interface WatcherService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.watcher.service.WatcherService#start()
	 */
	void start(CopyOnWriteArrayList<String> filesToBeProcess);

}