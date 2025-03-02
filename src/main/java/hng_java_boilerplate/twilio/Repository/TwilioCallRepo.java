package hng_java_boilerplate.twilio.Repository;

import hng_java_boilerplate.twilio.CallLogs.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwilioCallRepo extends JpaRepository<Entity,Long> {

}
