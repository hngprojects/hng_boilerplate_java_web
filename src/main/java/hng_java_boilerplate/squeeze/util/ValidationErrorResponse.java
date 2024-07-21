package hng_java_boilerplate.squeeze.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ValidationErrorResponse {
    private String message;
    private List<FieldError> errors;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String error;
    }
}

