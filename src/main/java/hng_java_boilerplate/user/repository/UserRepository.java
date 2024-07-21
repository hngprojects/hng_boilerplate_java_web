package hng_java_boilerplate.user.repository;

import hng_java_boilerplate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    Optional<User> findByUserId(String userId);

    boolean existsByEmail(String email);
}
