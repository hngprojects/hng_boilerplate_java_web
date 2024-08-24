package hng_java_boilerplate.statusPage.repository;

import hng_java_boilerplate.statusPage.entity.ApiStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiStatusRepository extends JpaRepository<ApiStatus, Long> {
    ApiStatus findByApiGroup(String apiGroup);
}