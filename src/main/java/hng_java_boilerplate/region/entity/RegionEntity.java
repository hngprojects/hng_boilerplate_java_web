package hng_java_boilerplate.region.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "Region")
@Schema(description = "Entity representing a region in the database")
public class RegionEntity {

    @Id
    @Column(name = "region_code", nullable = false, unique = true)
    @Schema(description = "Unique code for the region", example = "R001")
    private String regionCode;

    @Column(name = "region_name", nullable = false)
    @Schema(description = "Name of the region", example = "North Region")
    private String regionName;

    @Column(name = "status", nullable = false)
    @Schema(description = "Status of the region", example = "1")
    private int status;

    @Column(name = "created_on", nullable = false)
    @Schema(description = "Date and time when the region was created", example = "2024-07-24T15:30:00")
    private LocalDateTime createdOn;

    @Column(name = "created_by", nullable = false)
    @Schema(description = "Identifier of the user who created the region", example = "admin")
    private String createdBy;

    @Column(name = "modified_on", nullable = false)
    @Schema(description = "Date and time when the region was last modified", example = "2024-07-24T16:00:00")
    private LocalDateTime modifiedOn;

    @Column(name = "modified_by", nullable = false)
    @Schema(description = "Identifier of the user who last modified the region", example = "admin")
    private String modifiedBy;
}