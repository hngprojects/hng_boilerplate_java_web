package hng_java_boilerplate.SMS.SmsException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse <G>{
    private String status;
    private int status_code;
    private String message;

}
