package hng_java_boilerplate.externalPages.repository;

import hng_java_boilerplate.externalPages.entity.CustomSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomSectionRepository extends JpaRepository<CustomSection, Long> {
}
