package hng_java_boilerplate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity(name = "subscriptions")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Subscription {

    @Id
    private Long id;


}
