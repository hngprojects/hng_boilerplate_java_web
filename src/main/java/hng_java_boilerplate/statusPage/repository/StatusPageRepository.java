package hng_java_boilerplate.statusPage.repository;

import hng_java_boilerplate.statusPage.entity.StatusPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusPageRepository extends JpaRepository<StatusPage, Long> {
}