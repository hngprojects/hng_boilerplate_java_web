package hng_java_boilerplate;

import hng_java_boilerplate.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long>  {

}
