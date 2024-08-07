package hng_java_boilerplate.email.mailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;



@Component
@Slf4j
public class JavaMailServiceImpl implements JavaMailService{


    private final JavaMailSender emailSender;

    private final EmailUtils emailUtils;

    public JavaMailServiceImpl(JavaMailSender emailSender, EmailUtils emailUtils) {
        this.emailSender = emailSender;
        this.emailUtils = emailUtils;
    }


    public boolean sendMail(String to, String subject, String text)  {
        try{
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailUtils.APP_EMAIL);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            emailSender.send(mimeMessage);
            return true;
        } catch (Exception exception){
            log.info("exception message in the catch block<--> " + exception.getMessage());
            return false;
        }
    }



    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailUtils.APP_EMAIL);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file
                    = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);

            emailSender.send(message);

        } catch (MessagingException exception) {
            log.info(exception.getMessage());
        }

    }


}
