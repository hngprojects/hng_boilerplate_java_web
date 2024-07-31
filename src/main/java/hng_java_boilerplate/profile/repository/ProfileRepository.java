package hng_java_boilerplate.profile.repository;

import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, String> {
}
