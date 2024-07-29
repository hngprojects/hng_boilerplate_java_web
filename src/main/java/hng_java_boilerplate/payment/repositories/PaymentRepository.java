package hng_java_boilerplate.payment.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import hng_java_boilerplate.payment.entity.Payment;
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
