package watcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.watcher.fileprocessor.FileProcessor;
import com.watcher.fileprocessor.FileProcessorImpl;
import com.watcher.notification.NotificationByEmailImpl;
import com.watcher.notification.NotificationService;
import com.watcher.scheduler.Scheduler;
import com.watcher.scheduler.SchedulerImpl;
import com.watcher.service.WatcherService;
import com.watcher.service.WatcherServiceImpl;

@Configuration
//@ComponentScan({"com.watcher"})
@PropertySource("app.properties")
public class ApplicationConfig {
	
	@Bean(name = "watcherService")
	public WatcherService getWatcherService() {
		return new WatcherServiceImpl();
	}
	
	@Bean(name = "scheduler")
	public Scheduler getScheduler() {
		return new SchedulerImpl();
	}
	
	@Bean(name = "fileProcessor")
	public FileProcessor getFileProcessor() {
		return new FileProcessorImpl();
	}
	
	@Bean(name = "notificationService")
	public NotificationService getNotificationService() {
		return new NotificationByEmailImpl();
	}

	@Bean(name = "placeHolder")
	public PropertySourcesPlaceholderConfigurer getplaceholder() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
