package vinix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class HrEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrEurekaApplication.class, args);
	}

}
