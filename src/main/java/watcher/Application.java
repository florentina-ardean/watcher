package watcher;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.watcher.scheduler.Scheduler;
import com.watcher.service.WatcherService;

public class Application {
	public static CopyOnWriteArrayList<String> filesToProcess = new CopyOnWriteArrayList<String>();
	
	public static void main(String[] args) {

		ApplicationContext appContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		
		WatcherService watchService = appContext.getBean("watcherService", WatcherService.class);
		Scheduler scheduler = appContext.getBean("scheduler", Scheduler.class);
		
		scheduler.execute(filesToProcess);
		watchService.start(filesToProcess);
	}

}
