package hng_java_boilerplate.email.EmailServices;

import hng_java_boilerplate.email.entity.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailProducerService {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;


    public EmailProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailMessage(String to, String subject, String text) {
        log.info("Notification sent to Queue");
        EmailMessage emailMessage = new EmailMessage(to, subject, text);
        rabbitTemplate.convertAndSend(exchange, routingKey, emailMessage);
        log.info("Proceeding to the Consumer");
    }

}
