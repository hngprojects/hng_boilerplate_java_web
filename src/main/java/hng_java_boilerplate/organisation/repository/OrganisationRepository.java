package hng_java_boilerplate.organisation.repository;
import hng_java_boilerplate.organisation.entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrganisationRepository extends JpaRepository<Organisation, String> {

    @Query("SELECT o FROM Organisation o WHERE o.id = :id AND o.deleted = false")
    Optional<Organisation> findActiveById(String id);

    @Query("SELECT o FROM Organisation o WHERE o.name = :name AND o.deleted = false")
    List<Organisation> findByName(String name);

    @Query("SELECT o FROM Organisation o WHERE o.deleted = false")
    List<Organisation> findAllActive();
}