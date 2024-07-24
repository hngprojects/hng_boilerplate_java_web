package hng_java_boilerplate.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "sms_queue";
    @Value("${rabbitmq.exchange.key}")
    private String rabbitmq_exchange_key;
    @Value("${rabbitmq.routing.key}")
    private String rabbitmq_routing_key;

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(rabbitmq_exchange_key);
    }
    @Bean
    public Binding binding(){
            return BindingBuilder
                    .bind(queue())
                    .to(exchange())
                    .with(rabbitmq_routing_key);
    }

}

