package hng_java_boilerplate.profile.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileErrorResponseDto {

    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("message")
    private String message;
}
