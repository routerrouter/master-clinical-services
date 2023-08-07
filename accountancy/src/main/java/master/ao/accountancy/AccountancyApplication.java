package master.ao.accountancy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class AccountancyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountancyApplication.class, args);
	}

}
