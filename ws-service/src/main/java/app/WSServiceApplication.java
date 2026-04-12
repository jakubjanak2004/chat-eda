package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({"config.props", "app.config.props"})
public class WSServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WSServiceApplication.class, args);
	}

}
