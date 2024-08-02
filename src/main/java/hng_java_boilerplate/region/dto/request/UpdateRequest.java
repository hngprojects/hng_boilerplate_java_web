package hng_java_boilerplate.region.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateRequest {
    @NotBlank(message = "region is required")
    private String region;
    @NotBlank(message = "language is required")
    private String language;
    @NotBlank(message = "timezone is required")
    private String timezone;
}
