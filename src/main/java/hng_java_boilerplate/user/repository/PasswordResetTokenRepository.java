package hng_java_boilerplate.user.repository;


import hng_java_boilerplate.user.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUserId(String userId);
}
