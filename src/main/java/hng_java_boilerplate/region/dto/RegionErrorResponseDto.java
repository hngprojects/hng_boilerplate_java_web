package hng_java_boilerplate.region.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for Region error response")
public class RegionErrorResponseDto {

    @Schema(description = "HTTP status code of the error", example = "404")
    @JsonProperty("status_code")
    private String statusCode;

    @Schema(description = "Error message", example = "Region not found")
    @JsonProperty("message")
    private String message;
}


