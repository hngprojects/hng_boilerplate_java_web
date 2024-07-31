package hng_java_boilerplate.user.repository;

import hng_java_boilerplate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String username);

    Optional<User> findById(String id);

    boolean existsByEmail(String email);

    void deleteByEmail(String mail);
}
