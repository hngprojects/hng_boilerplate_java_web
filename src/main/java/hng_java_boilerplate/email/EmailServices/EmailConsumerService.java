package hng_java_boilerplate.email.EmailServices;

import hng_java_boilerplate.email.entity.EmailMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumerService {
    private final JavaMailSender emailSender;


    @Autowired
    public EmailConsumerService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void receiveMessage(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMessage.getSendTo());
        message.setSubject(emailMessage.getSubject());
        message.setText(emailMessage.getText());
        emailSender.send(message);
    }
}
