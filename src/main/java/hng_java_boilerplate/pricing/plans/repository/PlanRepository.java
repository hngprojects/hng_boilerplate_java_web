package hng_java_boilerplate.pricing.plans.repository;

import hng_java_boilerplate.pricing.plans.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, String> {
    boolean existsByName(String name);

}
