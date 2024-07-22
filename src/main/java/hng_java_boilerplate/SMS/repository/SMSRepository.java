package hng_java_boilerplate.SMS.repository;

import hng_java_boilerplate.SMS.entity.SMS;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SMSRepository extends JpaRepository<SMS, Long> {

}
