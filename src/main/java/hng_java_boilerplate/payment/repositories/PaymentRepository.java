package hng_java_boilerplate.payment.repositories;

import hng_java_boilerplate.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByUserEmail(String email);

}
