package hng_java_boilerplate.privacy_policy.repository;

import hng_java_boilerplate.privacy_policy.entity.PrivacyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivacyPolicyRepository extends JpaRepository<PrivacyPolicy, UUID> {
}
