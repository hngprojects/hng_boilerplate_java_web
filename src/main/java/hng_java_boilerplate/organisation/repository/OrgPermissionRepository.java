package hng_java_boilerplate.organisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hng_java_boilerplate.organisation.entity.OrgPermission;

import java.util.Optional;


public interface OrgPermissionRepository extends JpaRepository<OrgPermission, String> {
    Optional<OrgPermission> findByName(String name);
}
