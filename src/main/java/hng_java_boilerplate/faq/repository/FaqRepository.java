package hng_java_boilerplate.faq.repository;

import hng_java_boilerplate.faq.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<FAQ, String> {
}
