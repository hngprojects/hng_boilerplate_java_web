package hng_java_boilerplate.externalPages.repository;

import hng_java_boilerplate.externalPages.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
}
