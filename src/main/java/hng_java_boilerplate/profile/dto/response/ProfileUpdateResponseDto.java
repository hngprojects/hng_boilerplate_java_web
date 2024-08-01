package hng_java_boilerplate.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hng_java_boilerplate.profile.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateResponseDto {
    @JsonProperty("message")
    private String message;

    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("data")
    private Profile data;
}