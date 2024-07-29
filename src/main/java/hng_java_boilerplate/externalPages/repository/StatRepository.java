package hng_java_boilerplate.externalPages.repository;

import hng_java_boilerplate.externalPages.entity.StatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatRepository extends JpaRepository<StatEntity, Long> {
}
