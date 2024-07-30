package hng_java_boilerplate.profile.repository;

import hng_java_boilerplate.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, String> {
}
