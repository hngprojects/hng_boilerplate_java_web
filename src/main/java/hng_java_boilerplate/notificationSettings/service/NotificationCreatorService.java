package hng_java_boilerplate.notificationSettings.service;

import hng_java_boilerplate.notificationSettings.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationCreatorService {

    private RabbitTemplate rabbitTemplate;

    public NotificationCreatorService(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, message);
    }
}
