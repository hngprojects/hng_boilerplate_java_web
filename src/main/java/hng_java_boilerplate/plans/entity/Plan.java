package hng_java_boilerplate.plans.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import hng_java_boilerplate.plans.PlanType;
import hng_java_boilerplate.plans.util.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;



@Entity(name = "plans")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class Plan {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    @JsonProperty("duration_unit")
    private String durationUnit;

    @Convert(converter = StringListConverter.class)
    @Column(name = "features", nullable = false)
    private List<String> features = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type")
    private PlanType planType;


}
