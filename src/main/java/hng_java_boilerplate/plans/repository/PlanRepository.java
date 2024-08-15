package hng_java_boilerplate.plans.repository;

import hng_java_boilerplate.plans.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, String> {
    boolean existsByName(String name);

}
