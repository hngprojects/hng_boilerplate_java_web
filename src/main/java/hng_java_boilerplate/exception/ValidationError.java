package hng_java_boilerplate.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
    private int status_code;
    private String error;
    private Map<String, String> detail;

}
