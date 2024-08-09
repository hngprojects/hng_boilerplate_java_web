package hng_java_boilerplate.aboutpage.repository;

import hng_java_boilerplate.aboutpage.entity.AboutPageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutPageRepository extends JpaRepository<AboutPageContent, Long> {
}
