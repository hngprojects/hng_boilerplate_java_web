package hng_java_boilerplate.twilio.Repository;

import hng_java_boilerplate.twilio.CallLogs.CallLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwilioCallRepo extends JpaRepository<CallLogs,Long> {

}
