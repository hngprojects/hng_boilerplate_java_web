package hng_java_boilerplate.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponseDto {

    private String message;
    private String error;
    private int status_code;
}
