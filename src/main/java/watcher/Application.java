package watcher;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.watcher.service.WatcherService;

public class Application {

	public static void main(String[] args) {
//		WatcherService watcher = new WatcherServiceImpl();
//		watcher.start();
		
		ApplicationContext appContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		WatcherService wService = appContext.getBean("watcherService", WatcherService.class);
		wService.start();
	}

}
