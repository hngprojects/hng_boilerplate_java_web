package hng_java_boilerplate.email.repository;

import hng_java_boilerplate.email.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, String> {
    boolean existsByTitle(String title);
    Optional<EmailTemplate> findByTitle(String title);
}
