package hng_java_boilerplate.region.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Schema(description = "Entity representing user region information in the database")
public class UserRegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id", nullable = false)
    @Schema(description = "Unique identifier for the user region record", example = "1")
    private Integer regionId;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "Unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Column(name = "region_name", nullable = false)
    @Schema(description = "Name of the region", example = "North Region")
    private String regionName;

    @Column(name = "region_code", nullable = false)
    @Schema(description = "Code of the region", example = "NR")
    private String regionCode;
}

