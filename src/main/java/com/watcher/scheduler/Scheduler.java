package com.watcher.scheduler;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Scheduler {

	void execute(CopyOnWriteArrayList<String> filesToBeProcessed);

}