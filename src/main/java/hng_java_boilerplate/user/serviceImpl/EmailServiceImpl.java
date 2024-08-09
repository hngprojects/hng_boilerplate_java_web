package hng_java_boilerplate.user.serviceImpl;

import hng_java_boilerplate.email.EmailServices.EmailTemplateService;
import hng_java_boilerplate.email.dto.EmailTemplateResponse;
import hng_java_boilerplate.email.exception.EmailTemplateNotFound;
import hng_java_boilerplate.user.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl {

    @Value("${app.host.baseurl:http://localhost:3000}")
    private String baseUrl;

    private final JavaMailSender javaMailSender;
    private final EmailTemplateService emailTemplateService;

    public String applicationUrl(HttpServletRequest request){
        return baseUrl + "/api/v1/auth" + request.getContextPath();
    }

    public void passwordResetTokenMail(User user, HttpServletRequest request, String token) {
        String url = applicationUrl(request) + "/reset-password/" + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Password Reset Email");

            String template = getTemplateContent("password-reset");
            String emailBody = template.replace("${name}", user.getName())
                    .replace("${url}", url)
                    .replace("${token}", token);

            helper.setText(emailBody, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendMagicLink(String email, HttpServletRequest request, String token) {
        String url = applicationUrl(request) + "/magic-link/login?token=" + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Your Magic Link");

            String template = getTemplateContent("magic-link");
            String emailBody = template.replace("${url}", url)
                    .replace("${token}", token);

            helper.setText(emailBody, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendVerificationEmail(User user, HttpServletRequest request, String token) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Verification Token");

            String template = getTemplateContent("email-verification");
            String emailBody = template.replace("${name}", user.getName())
                    .replace("${token}", token);

            helper.setText(emailBody, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getTemplateContent(String templateName) {
        try {
            EmailTemplateResponse response = emailTemplateService.getTemplate(templateName).getBody();
            if (response != null && response.data() != null) {
                return response.data().getTemplate();
            } else {
                throw new EmailTemplateNotFound("Template content is null for template name: " + templateName);
            }
        } catch (Exception e) {
            throw new EmailTemplateNotFound("Failed to retrieve template content for template name: " + templateName);
        }
    }

    public String generateToken() {
        Random random = new Random();
        int length = 6;
        StringBuilder suffixBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            suffixBuilder.append(random.nextInt(10));
        }
        return suffixBuilder.toString();
    }
}
