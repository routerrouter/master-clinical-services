package master.ao.authuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AuthuserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthuserApplication.class, args);
    }

}
