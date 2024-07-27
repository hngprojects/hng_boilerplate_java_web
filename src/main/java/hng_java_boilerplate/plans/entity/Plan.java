package hng_java_boilerplate.plans.entity;

import hng_java_boilerplate.plans.util.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    private String durationUnit;

    @Convert(converter = StringListConverter.class)
    @Column(name = "features", nullable = false)
    private List<String> features = new ArrayList<>();
}
