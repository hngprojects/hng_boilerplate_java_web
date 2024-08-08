package hng_java_boilerplate.email.EmailServices;

import hng_java_boilerplate.email.entity.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumerService {
    private final JavaMailSender emailSender;

    @Value("${rabbitmq.queue.email}")
    private String emailQueue;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public EmailConsumerService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void receiveMessage(EmailMessage emailMessage) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getText(), true);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
