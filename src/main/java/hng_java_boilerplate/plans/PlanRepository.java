package hng_java_boilerplate.plans;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    boolean existsByName(String name);

}
