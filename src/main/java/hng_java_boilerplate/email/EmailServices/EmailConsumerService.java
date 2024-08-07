package hng_java_boilerplate.email.EmailServices;

import hng_java_boilerplate.email.entity.EmailMessage;
import hng_java_boilerplate.email.mailSender.JavaMailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailConsumerService {

    private final JavaMailService javaMailService;

    @Autowired
    public EmailConsumerService(JavaMailService javaMailService) {
        this.javaMailService = javaMailService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void receiveMessage(EmailMessage emailMessage) throws MessagingException {
        log.info("Consumer received the message well");
        javaMailService.sendMail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getText());
        log.info("Notification delivered");
    }




}
