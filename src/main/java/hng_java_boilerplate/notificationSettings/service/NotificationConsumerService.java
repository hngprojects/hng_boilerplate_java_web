package hng_java_boilerplate.notificationSettings.service;

import hng_java_boilerplate.notificationSettings.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class NotificationConsumerService {
@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveNotification(String message){
        System.out.println("Received message:" + message);
    }
}
