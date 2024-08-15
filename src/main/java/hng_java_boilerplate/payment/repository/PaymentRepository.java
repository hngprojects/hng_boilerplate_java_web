package hng_java_boilerplate.payment.repository;

import hng_java_boilerplate.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
}
