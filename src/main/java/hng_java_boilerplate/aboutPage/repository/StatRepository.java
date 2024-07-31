package hng_java_boilerplate.aboutPage.repository;

import hng_java_boilerplate.aboutPage.entities.StatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatRepository extends JpaRepository<StatEntity, Long> {
}
