package hng_java_boilerplate.privacy_policy.service;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.privacy_policy.entity.PrivacyPolicy;
import hng_java_boilerplate.privacy_policy.repository.PrivacyPolicyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrivacyPolicyServiceImpl implements PrivacyPolicyService {

    private final PrivacyPolicyRepository privacyPolicyRepository;

    public PrivacyPolicy createPrivacyPolicy(PrivacyPolicy newPolicy) {
        newPolicy.setCreatedAt(LocalDateTime.now());
        newPolicy.setUpdatedAt(LocalDateTime.now());
        return privacyPolicyRepository.save(newPolicy);
    }

    public List<PrivacyPolicy> getAllPrivacyPolicies() {
        return privacyPolicyRepository.findAll();
    }

    public PrivacyPolicy getPrivacyPolicyById(UUID id) {
        return privacyPolicyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Privacy policy not found."));
    }

    public PrivacyPolicy updatePrivacyPolicy(UUID id, PrivacyPolicy updatedPolicy) {
        Optional<PrivacyPolicy> existingPolicyOpt = privacyPolicyRepository.findById(id);
        if (existingPolicyOpt.isEmpty()) {
            throw new NotFoundException("Privacy policy not found.");
        }
        PrivacyPolicy existingPolicy = existingPolicyOpt.get();
        existingPolicy.setContent(updatedPolicy.getContent());
        existingPolicy.setUpdatedAt(LocalDateTime.now());
        return privacyPolicyRepository.save(existingPolicy);
    }

    @Transactional
    public void deletePrivacyPolicy(UUID id) {
        if (!privacyPolicyRepository.existsById(id)) {
            throw new NotFoundException("Privacy policy not found.");
        }
        privacyPolicyRepository.deleteById(id);
    }
}
