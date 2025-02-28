package hng_java_boilerplate.newsletter.repository;

import hng_java_boilerplate.newsletter.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
}
