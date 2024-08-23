package hng_java_boilerplate.blog.squeeze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ValidationErrorResponseDto {
    private String message;
    private List<FieldErrorDto> errors;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class FieldErrorDto {
        private String field;
        private String error;
    }
}

