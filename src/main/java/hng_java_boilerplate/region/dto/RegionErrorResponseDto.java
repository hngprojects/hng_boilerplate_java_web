package hng_java_boilerplate.region.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionErrorResponseDto {

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("message")
    private String message;
}


