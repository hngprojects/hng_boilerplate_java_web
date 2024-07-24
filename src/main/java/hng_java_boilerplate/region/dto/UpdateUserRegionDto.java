package hng_java_boilerplate.region.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for updating user region")
public class UpdateUserRegionDto {

    @Schema(description = "Name of the region", example = "North Region")
    @JsonProperty("region_name")
    private String regionName;

    @Schema(description = "Country code of the region", example = "US")
    @JsonProperty("country_code")
    private String countryCode;
}