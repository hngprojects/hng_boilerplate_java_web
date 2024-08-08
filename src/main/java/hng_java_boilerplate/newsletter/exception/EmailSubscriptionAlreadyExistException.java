package hng_java_boilerplate.newsletter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmailSubscriptionAlreadyExistException extends  RuntimeException{

    public EmailSubscriptionAlreadyExistException(String message) {
        super(message);
    }
}
