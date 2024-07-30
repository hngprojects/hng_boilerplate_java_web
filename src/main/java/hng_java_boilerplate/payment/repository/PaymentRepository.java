package hng_java_boilerplate.payment.repository;

import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    List<Payment> findByUserEmail(String email);

    Optional<Payment> findByUserEmailAndTransactionReference(String email, String reference);

    Optional<Payment> findByTransactionReference(String reference);

}
