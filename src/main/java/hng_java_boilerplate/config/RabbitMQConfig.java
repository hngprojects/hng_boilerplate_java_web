//package hng_java_boilerplate.config;
//
//import org.springframework.amqp.core.Queue;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//
//    @Value("${rabbitmq.queue.email}")
//    private String emailQueueName;
//
//    @Bean
//    public Queue emailQueue() {
//        return new Queue(emailQueueName, true);
//    }
//}
