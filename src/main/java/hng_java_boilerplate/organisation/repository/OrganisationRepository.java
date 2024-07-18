package hng_java_boilerplate.organisation.repository;

import hng_java_boilerplate.organisation.entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationRepository extends JpaRepository<Organisation, String> {
}
