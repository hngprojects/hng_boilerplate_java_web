package hng_java_boilerplate.email.mailSender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {


    @Value("${app.email}")
    public String APP_EMAIL;

    @Value("${app.password}")
    public String APP_PASSWORD;

}
