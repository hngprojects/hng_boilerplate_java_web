package hng_java_boilerplate.blog.squeeze.repository;

import hng_java_boilerplate.blog.squeeze.entity.SqueezeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SqueezeRequestRepository extends JpaRepository<SqueezeRequest, UUID> {
    boolean existsByEmail(String email);
    Optional<SqueezeRequest> findByEmail(String email);
}
