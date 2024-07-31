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

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;


}
