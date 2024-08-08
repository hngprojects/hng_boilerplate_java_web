package hng_java_boilerplate.newsletter.repository;

import hng_java_boilerplate.newsletter.entity.NewsletterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface NewsletterSubscriptionRepository extends JpaRepository<NewsletterSubscription, String> {

    Optional<NewsletterSubscription> findByEmail(String email);

}
