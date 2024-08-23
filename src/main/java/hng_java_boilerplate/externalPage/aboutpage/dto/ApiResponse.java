package hng_java_boilerplate.externalPage.aboutpage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("status_code")
    private int statusCode;

    public ApiResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
