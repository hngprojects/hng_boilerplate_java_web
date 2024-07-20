package hng_java_boilerplate.organisation.exception.response;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
public class SuccessResponse {

    private String message;
    private int status;
}
