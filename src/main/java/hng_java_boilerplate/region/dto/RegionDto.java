package hng_java_boilerplate.region.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for Region")
public class RegionDto {

    @Schema(description = "Code of the region", example = "123")
    @JsonProperty("region_code")
    private String regionCode;

    @Schema(description = "Name of the region", example = "North Region")
    @JsonProperty("region_name")
    private String regionName;

    @Schema(description = "Status of the region", example = "1")
    @JsonProperty("status")
    private int status;

    @Schema(description = "Creation timestamp of the region", example = "2023-01-01T12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("created_on")
    private LocalDateTime createdOn;

    @Schema(description = "User who created the region", example = "admin")
    @JsonProperty("created_by")
    private String createdBy;

    @Schema(description = "Last modification timestamp of the region", example = "2023-01-01T12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("modified_on")
    private LocalDateTime modifiedOn;

    @Schema(description = "User who last modified the region", example = "admin")
    @JsonProperty("modified_by")
    private String modifiedBy;
}