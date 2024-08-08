package hng_java_boilerplate.activitylog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "activity_logs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_id", nullable = false)
    private String orgId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "activity", nullable = false)
    private String activity;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}
