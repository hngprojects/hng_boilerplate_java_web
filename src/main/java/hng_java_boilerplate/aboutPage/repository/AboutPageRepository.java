package hng_java_boilerplate.aboutPage.repository;

import hng_java_boilerplate.aboutPage.entities.AboutPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutPageRepository extends JpaRepository<AboutPage, Long> {
}
