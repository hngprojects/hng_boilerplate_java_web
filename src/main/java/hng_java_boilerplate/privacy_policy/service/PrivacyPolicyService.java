package hng_java_boilerplate.privacy_policy.service;

import hng_java_boilerplate.privacy_policy.entity.PrivacyPolicy;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PrivacyPolicyService {

    PrivacyPolicy createPrivacyPolicy(PrivacyPolicy newPolicy);

    List<PrivacyPolicy> getAllPrivacyPolicies();

    PrivacyPolicy getPrivacyPolicyById(UUID id);

    PrivacyPolicy updatePrivacyPolicy(UUID id, PrivacyPolicy updatedPolicy);

    @Transactional
    void deletePrivacyPolicy(UUID id);
}
