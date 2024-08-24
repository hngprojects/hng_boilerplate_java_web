package hng_java_boilerplate.newsletter.repository;

import hng_java_boilerplate.newsletter.entity.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterRepository extends JpaRepository<Newsletter, String> {
}
