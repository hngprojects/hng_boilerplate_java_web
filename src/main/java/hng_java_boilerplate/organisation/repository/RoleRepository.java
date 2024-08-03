package hng_java_boilerplate.organisation.repository;

import hng_java_boilerplate.organisation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, String> {
    List<Role> findByOrganisationId(String orgId);

    Optional<Role> findByIdAndOrganisationId(String roleId, String organisationId);
}
