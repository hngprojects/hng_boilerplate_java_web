package hng_java_boilerplate.user.repository;

import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String username);

    Optional<User> findById(String id);

    boolean existsByEmail(String email);

    void deleteByEmail(String mail);

    List<User> findUserByUserRole(Role role);

    Page<User> findByOrganisations_Id( String orgId, Pageable pageable);
}
