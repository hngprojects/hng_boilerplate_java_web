package hng_java_boilerplate.squeeze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseMessageDto {
    private String message;
    private int status_code;
}

