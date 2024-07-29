package hng_java_boilerplate.plans.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import hng_java_boilerplate.plans.enums.Category;
import hng_java_boilerplate.plans.enums.MembershipType;
import hng_java_boilerplate.plans.util.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


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

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @JsonProperty("duration_unit")
    private String durationUnit;

    @Convert(converter = StringListConverter.class)
    @Column(name = "features", nullable = false)
    private List<String> features = new ArrayList<>();
}
