package hng_java_boilerplate.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue emailQueue() {
        return new Queue("emailQueue", true);
    }

}
