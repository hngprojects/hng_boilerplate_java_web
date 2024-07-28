package hng_java_boilerplate.organisation.exception;

import java.util.List;

public record ExceptionResponse(
        List<ValidationError> errors
) {
}
