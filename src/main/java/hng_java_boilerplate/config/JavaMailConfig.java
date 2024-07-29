package hng_java_boilerplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaMailConfig {

    @Bean
    public JavaMailSender javaMailCofig() {
        return new JavaMailSenderImpl();
    }

}
