package hng_java_boilerplate.region.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRegionDto {

    @JsonProperty("region_name")
    private String regionName;

    @JsonProperty("country_code")
    private String countryCode;
}
