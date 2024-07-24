package hng_java_boilerplate.plans.repository;

import hng_java_boilerplate.plans.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    boolean existsByName(String name);

}
