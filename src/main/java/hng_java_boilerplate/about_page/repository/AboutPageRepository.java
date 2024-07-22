package hng_java_boilerplate.about_page.repository;

import hng_java_boilerplate.about_page.entity.AboutPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutPageRepository extends JpaRepository<AboutPage, Long> {
}
