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
    private String api_group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime last_checked;

    private Integer response_time;

    private String details;

    public enum Status {
        OPERATIONAL, DEGRADED, DOWN
    }
}