package hng_java_boilerplate.SMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponseDto {
    private String status;
    private int status_code;
    private String message;

}
