package hng_java_boilerplate.user.serviceImpl;

import hng_java_boilerplate.email.EmailServices.EmailTemplateService;
import hng_java_boilerplate.email.dto.EmailTemplateResponse;
import hng_java_boilerplate.exception.exception_class.BadRequestException;
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
        Map<String, String> variables = new HashMap<>();
        variables.put("name", user.getName());
        variables.put("url", url);

        String emailBody = getTemplateContent("password-reset-template", variables);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Password Reset Email");
            helper.setText(emailBody, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMagicLink(String email, HttpServletRequest request, String token) {
        String url = applicationUrl(request) + "/magic-link/login?token=" + token;
        Map<String, String> variables = new HashMap<>();
        variables.put("url", url);

        String emailBody = getTemplateContent("magic-link-template", variables);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Your Magic Link");
            helper.setText(emailBody, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmail(User user, HttpServletRequest request, String token) {
        Map<String, String> variables = new HashMap<>();
        variables.put("name", user.getName());
        variables.put("token", token);

        String emailBody = getTemplateContent("verification-template", variables);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Verification Token");

            helper.setText(emailBody, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Email Not sent");
        }
    }

    private String getTemplateContent(String templateName, Map<String, String> variables) {
        try {
            EmailTemplateResponse response = emailTemplateService.getTemplate(templateName).getBody();

            if (response != null && response.data() != null) {
                return replacePlaceholders(response.data().getTemplate(), variables);
            } else {
                return replacePlaceholders(getDefaultTemplate(templateName), variables);
            }
        } catch (Exception e) {
            return replacePlaceholders(getDefaultTemplate(templateName), variables);
        }
    }

    private String replacePlaceholders(String template, Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            template = template.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return template;
    }


    private String getDefaultTemplate(String templateName) {
        switch (templateName) {
            case "password-reset-template":
                return "<div style=\"width: 50%; margin: 0 auto; background-color: white; color: black;\">" +
                        "<div><img style=\"width: 100%; display: inline-block;\" src=\"cid:reset\"></div><br><br>" +
                        "<h1 style=\"text-align: center; color: #F97316;\">Dear ${name}!</h1><br>" +
                        "<h2>You recently requested to reset your password. <br>To proceed with the password reset process, please click on the button below:</h2> <br>" +
                        "<div style=\"text-align: center;\"><a href=\"${url}\" style=\"background-color: #F97316; color: white; text-decoration: none; padding: 10px 20px; border-radius: 10px; cursor: pointer; display: inline-block; width: 250px; height: 35px; font-size: 20px;\">Reset Password</a></div><br>" +
                        "<h3>If you did not request this change, please ignore this email or contact our support team immediately.</h3>" +
                        "<h3>Thank you.</h3>" +
                        "<h2>Best regards,<br>HNG</h2></div>";

            case "magic-link-template":
                return "<div style=\"width: 50%; margin: 0 auto; background-color: white; color: black;\">" +
                        "<div><img style=\"width: 100%; display: inline-block;\" src=\"cid:magic-link\"></div><br><br>" +
                        "<h1 style=\"text-align: left; color: black;\"> Hello,</h1><br>" +
                        "<h2>To log in to your account, please click on the button below:</h2> <br>" +
                        "<div style=\"text-align: center;\"><a href=\"${url}\" style=\"background-color: #F97316; color: white; text-decoration: none; padding: 10px 20px; border-radius: 10px; cursor: pointer; display: inline-block; width: 250px; height: 35px; font-size: 20px;\">Log In</a></div><br>" +
                        "<h3>If you did not request this link, please ignore this email or contact our support team immediately.</h3>" +
                        "<h3>Thank you.</h3>" +
                        "<h2>Best regards,<br>HNG</h2></div>";

            case "verification-template":
                return "<div style=\"width: 50%; margin: 0 auto; background-color: #ffffff; color: #000000; font-family: Arial, sans-serif;\">" +
                        "<div style=\"text-align: center;\"><img style=\"width: 100%; display: block;\" src=\"cid:welcomeVerify\" alt=\"Welcome Image\"></div><br><br>" +
                        "<h1 style=\"text-align: center; color: #F97316; font-size: 36px;\">Welcome ${name}!</h1><br>" +
                        "<h2 style=\"text-align: center; font-size: 18px;\">You're almost ready to get started. Use the 6-digit code below to verify your account.<br>This OTP expires in 60 minutes.</h2><br><br>" +
                        "<div style=\"text-align: center; align-item: center; background-color: #ffffff; color: #F97316; text-decoration: none; padding: 15px 30px; border-radius: 8px; display: inline-block; font-size: 28px; font-weight: bold;\">" + "${token}" + "</div><br>" +
                        "<h2>Best regards,<br>HNG</h2></div>";

            default:
                throw new BadRequestException("Unknown template: " + templateName);
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
