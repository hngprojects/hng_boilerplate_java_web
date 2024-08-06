package hng_java_boilerplate.user.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Order(1)
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedUserException extends RuntimeException{
    public UnAuthorizedUserException(String message) {
        super(message);
    }
}
