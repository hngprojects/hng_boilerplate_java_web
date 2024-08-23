package hng_java_boilerplate.authentication.profile.repository;

import hng_java_boilerplate.authentication.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, String> {
}
