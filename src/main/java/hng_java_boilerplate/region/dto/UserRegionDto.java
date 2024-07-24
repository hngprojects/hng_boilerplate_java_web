package hng_java_boilerplate.region.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for user region")
public class UserRegionDto {

    @Schema(description = "ID of the region", example = "1")
    @JsonProperty("region_id")
    private Integer regionId;

    @Schema(description = "ID of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty("user_id")
    private UUID userId;

    @Schema(description = "Name of the region", example = "North Region")
    @JsonProperty("region_name")
    private String regionName;

    @Schema(description = "Country code of the region", example = "US")
    @JsonProperty("country_code")
    private String countryCode;
}