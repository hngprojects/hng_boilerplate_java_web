package hng_java_boilerplate.squeeze.repository;

import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SqueezeRequestRepository extends JpaRepository<SqueezeRequest, UUID> {
    boolean existsByEmail(String email);
}
