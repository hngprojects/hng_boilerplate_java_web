package hng_java_boilerplate.organisation.repository;

import hng_java_boilerplate.organisation.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
