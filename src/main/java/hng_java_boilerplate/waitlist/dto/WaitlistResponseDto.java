package hng_java_boilerplate.waitlist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaitlistResponseDto {

    @JsonProperty("message")
    private String message;

    @JsonProperty("status_code")
    private Integer statusCode;

    @JsonProperty("error")
    private String error;

    public WaitlistResponseDto(String message) {
        this.message = message;
    }

    public WaitlistResponseDto(String message, int statusCode, String error) {
        this.message = message;
        this.statusCode = statusCode;
        this.error = error;
    }
}
