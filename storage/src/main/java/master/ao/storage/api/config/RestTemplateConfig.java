package master.ao.storage.api.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    static final int TIMEOUT = 5000;

    //@LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder
                //.setConnectTimeout(Duration.ofMillis(TIMEOUT))
                //.setReadTimeout(Duration.ofMillis(TIMEOUT))
                .build();
    }
}
