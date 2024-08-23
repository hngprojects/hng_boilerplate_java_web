package hng_java_boilerplate.statusPage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "api_status")
public class ApiStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String apiGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime lastChecked;

    private Integer responseTime;

    private String details;

    public enum Status {
        OPERATIONAL, DEGRADED, DOWN
    }
}