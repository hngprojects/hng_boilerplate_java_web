package hng_java_boilerplate.SMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequestDto {
    private String phone_number;
    private String message;

}
