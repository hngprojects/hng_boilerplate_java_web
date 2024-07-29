package hng_java_boilerplate.externalPages.repository;

import hng_java_boilerplate.externalPages.entity.AboutPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutPageRepository extends JpaRepository<AboutPage, Long> {
}
