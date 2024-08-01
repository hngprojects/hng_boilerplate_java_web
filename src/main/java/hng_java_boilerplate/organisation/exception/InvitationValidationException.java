package hng_java_boilerplate.organisation.exception;

import lombok.Data;

import java.util.List;
@Data
public class InvitationValidationException extends RuntimeException {

    List<String> error;
    Integer status;

    public InvitationValidationException(String message, List<String> error, Integer status) {
        super(message);
        this.error = error;
        this.status = status;
    }
}
