package hng_java_boilerplate.organisation.repository;

import hng_java_boilerplate.organisation.entity.OrgRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrgRoleRepository extends JpaRepository<OrgRole, String> {
    Optional<OrgRole> findByName(String name);
}
