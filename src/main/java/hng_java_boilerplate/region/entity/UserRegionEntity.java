package hng_java_boilerplate.region.entity;


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
@Table(name = "user_region")
public class UserRegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id", nullable = false)
    private Integer regionId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "region_name", nullable = false)
    private String regionName;

    @Column(name = "region_code", nullable = false)
    private String regionCode;
}

