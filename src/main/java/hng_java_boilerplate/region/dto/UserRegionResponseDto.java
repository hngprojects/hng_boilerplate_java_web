package hng_java_boilerplate.region.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegionResponseDto {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("region_name")
    private String regionName;

    @JsonProperty("country_code")
    private String countryCode;
}
