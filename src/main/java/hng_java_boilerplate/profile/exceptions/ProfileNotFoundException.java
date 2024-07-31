package hng_java_boilerplate.profile.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProfileNotFoundException extends RuntimeException{
    public ProfileNotFoundException(String message) {
        super(message);
    }
}