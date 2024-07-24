package hng_java_boilerplate.plans.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder()
@Table(name = "plans")
public class Plan {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String duration_unit;
}
