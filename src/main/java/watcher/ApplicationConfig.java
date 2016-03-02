package watcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.watcher.service.WatcherService;
import com.watcher.service.WatcherServiceImpl;

@Configuration
@ComponentScan({"com.watcher"})
//@PropertySource("app.properties")
public class ApplicationConfig {
	
	@Bean(name = "watcherService")
	public WatcherService getWatcherService() {
		return new WatcherServiceImpl();
	}
	
//	@Bean(name = "notificationService")
//	public NotificationService getNotificationService() {
//		return new NotificationByEmailImpl();
//	}

//	@Bean(name = "placeHolder")
//	public PropertySourcesPlaceholderConfigurer getplaceholder() {
//		return new PropertySourcesPlaceholderConfigurer();
//	}
}
