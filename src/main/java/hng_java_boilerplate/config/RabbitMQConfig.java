package hng_java_boilerplate.config;

import hng_java_boilerplate.SMS.messageQueueService.SmsReceiver;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "sms_queue";
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }
    @Bean
    public MessageListenerAdapter listenerAdapter(SmsReceiver smsReceiver) {
        return new MessageListenerAdapter(smsReceiver, "receiveMessage");
    }
}

