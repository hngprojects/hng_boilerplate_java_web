package hng_java_boilerplate.organisation.repository;

import hng_java_boilerplate.organisation.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, String> {
}
