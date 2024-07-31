package hng_java_boilerplate.aboutPage.repository;

import hng_java_boilerplate.aboutPage.entities.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
}
