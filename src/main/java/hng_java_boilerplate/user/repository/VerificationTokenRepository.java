package hng_java_boilerplate.user.repository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
