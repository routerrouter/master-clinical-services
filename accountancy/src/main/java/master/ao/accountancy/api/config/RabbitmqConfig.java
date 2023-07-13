package master.ao.accountancy.api.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    /*@Autowired
    CachingConnectionFactory cachingConnectionFactory;

    @Value(value = "${ead.broker.exchange.userEvent}")
    private String exchangeUserEvent;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public FanoutExchange fanoutUserEvent() {
        return new FanoutExchange(exchangeUserEvent);
    }*/

}
