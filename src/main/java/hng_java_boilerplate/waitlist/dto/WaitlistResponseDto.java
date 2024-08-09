package hng_java_boilerplate.waitlist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class WaitlistResponseDto {
    @JsonProperty("message")
    private String message;

    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("error")
    private String error;


    public WaitlistResponseDto(String message) {
        this.message = message;
        this.statusCode = 201;
    }


    public WaitlistResponseDto(String message, int statusCode, String error) {
        this.message = message;
        this.statusCode = statusCode;
        this.error = error;
    }
}
