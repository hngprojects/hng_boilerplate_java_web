package hng_java_boilerplate.region.entity;


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
public class RegionEntity {

    @Id
    @Column(name = "region_code", nullable = false, unique = true)
    private String regionCode;

    @Column(name = "region_name", nullable = false)
    private String regionName;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_on", nullable = false)
    private LocalDateTime modifiedOn;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;
}


