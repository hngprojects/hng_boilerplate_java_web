package hng_java_boilerplate.user.repository;

import hng_java_boilerplate.user.entity.MagicLinkToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagicLinkTokenRepository extends JpaRepository<MagicLinkToken, String> {
    MagicLinkToken findByToken(String token);
    MagicLinkToken findByUserId(String id);
}
