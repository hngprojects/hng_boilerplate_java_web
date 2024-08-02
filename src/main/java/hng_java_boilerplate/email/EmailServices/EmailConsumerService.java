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

    @Value("${rabbitmq.queue.email}")
    private String emailQueue;

    @Autowired
    public EmailConsumerService(JavaMailSender emailSender) {
<<<<<<< HEAD

=======
>>>>>>> 8dc808a6764e85aef90f38542703156ec487c73d
        this.emailSender = emailSender;
    }

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void receiveMessage(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMessage.getTo());
        message.setSubject(emailMessage.getSubject());
        message.setText(emailMessage.getText());
        emailSender.send(message);
    }
}
