package watcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ComponentScan({"com.watcher"})
@PropertySource("app.properties")
public class ApplicationConfig {
	
	@Bean(name = "mailSender")
	public MailSender getMailSender() {
		return new JavaMailSenderImpl();
	}
	
	@Bean(name = "placeHolder")
	public PropertySourcesPlaceholderConfigurer getplaceholder() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
