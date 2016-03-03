package watcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.watcher.notification.NotificationByEmailSpringImpl;
import com.watcher.notification.NotificationService;

@Configuration
//@ComponentScan({"com.watcher"})
@PropertySource("app.properties")
public class ApplicationConfigTestNotification {
//	
//	@Bean(name = "watcherService")
//	public WatcherService getWatcherService() {
//		return new WatcherServiceImpl();
//	}
//	
//	@Bean(name = "scheduler")
//	public Scheduler getScheduler() {
//		return new SchedulerImpl();
//	}
//	
//	@Bean(name = "fileProcessor")
//	public FileProcessor getFileProcessor() {
//		return new FileProcessorImpl();
//	}
	
	@Bean(name = "mailSender")
	public MailSender getMailSender() {
		return new JavaMailSenderImpl();
	}
	
	@Bean(name = "notificationService")
	public NotificationService getNotificationService() {
		return new NotificationByEmailSpringImpl();
	}

	@Bean(name = "placeHolder")
	public PropertySourcesPlaceholderConfigurer getplaceholder() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
