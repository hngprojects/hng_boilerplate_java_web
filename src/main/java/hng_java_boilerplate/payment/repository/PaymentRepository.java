package hng_java_boilerplate.payment.repository;

import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
//    Set<Payment> findByUser_IdAndOrderByCreatedAtDesc(String id);
}
