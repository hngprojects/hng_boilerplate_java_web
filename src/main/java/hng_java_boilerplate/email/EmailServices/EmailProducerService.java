//package hng_java_boilerplate.email.EmailServices;
//
//import hng_java_boilerplate.email.entity.EmailMessage;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailProducerService {
//    private final RabbitTemplate rabbitTemplate;
//
//    @Value("${rabbitmq.queue.email}")
//    private String emailQueue;
//
//    @Autowired
//    public EmailProducerService(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void sendEmailMessage(String to, String subject, String text) {
//        EmailMessage emailMessage = new EmailMessage(to, subject, text);
//        rabbitTemplate.convertAndSend(emailQueue, emailMessage);
//    }
//}
