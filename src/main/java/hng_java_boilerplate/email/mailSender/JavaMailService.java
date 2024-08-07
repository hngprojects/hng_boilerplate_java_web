package hng_java_boilerplate.email.mailSender;

import jakarta.mail.MessagingException;

public interface JavaMailService {

    public boolean sendMail(String to, String subject, String text) throws MessagingException;
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException;

}
