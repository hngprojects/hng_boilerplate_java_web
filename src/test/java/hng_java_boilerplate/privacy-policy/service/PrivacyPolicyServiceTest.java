package hng_java_boilerplate.privacy_policy.service;

import hng_java_boilerplate.privacy_policy.entity.PrivacyPolicy;
import hng_java_boilerplate.privacy_policy.exception.PrivacyPolicyNotFoundException;
import hng_java_boilerplate.privacy_policy.repository.PrivacyPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrivacyPolicyServiceTest {

    @Mock
    private PrivacyPolicyRepository privacyPolicyRepository;

    @InjectMocks
    private PrivacyPolicyService privacyPolicyService;

    private PrivacyPolicy privacyPolicy;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        privacyPolicy = new PrivacyPolicy();
        privacyPolicy.setId(id);
        privacyPolicy.setContent("Test Content");
        privacyPolicy.setCreatedAt(LocalDateTime.now());
        privacyPolicy.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createPrivacyPolicy_ShouldReturnSavedPolicy() {
        when(privacyPolicyRepository.save(any(PrivacyPolicy.class))).thenReturn(privacyPolicy);

        PrivacyPolicy createdPolicy = privacyPolicyService.createPrivacyPolicy(privacyPolicy);

        assertNotNull(createdPolicy);
        assertEquals("Test Content", createdPolicy.getContent());
        verify(privacyPolicyRepository, times(1)).save(privacyPolicy);
    }

    @Test
    void getAllPrivacyPolicies_ShouldReturnListOfPolicies() {
        when(privacyPolicyRepository.findAll()).thenReturn(List.of(privacyPolicy));

        List<PrivacyPolicy> policies = privacyPolicyService.getAllPrivacyPolicies();

        assertFalse(policies.isEmpty());
        assertEquals(1, policies.size());
        verify(privacyPolicyRepository, times(1)).findAll();
    }

    @Test
    void getPrivacyPolicyById_ShouldReturnPolicy_WhenPolicyExists() {
        when(privacyPolicyRepository.findById(id)).thenReturn(Optional.of(privacyPolicy));

        PrivacyPolicy foundPolicy = privacyPolicyService.getPrivacyPolicyById(id);

        assertNotNull(foundPolicy);
        assertEquals("Test Content", foundPolicy.getContent());
        verify(privacyPolicyRepository, times(1)).findById(id);
    }

    @Test
    void getPrivacyPolicyById_ShouldThrowException_WhenPolicyDoesNotExist() {
        when(privacyPolicyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PrivacyPolicyNotFoundException.class, () -> privacyPolicyService.getPrivacyPolicyById(id));
        verify(privacyPolicyRepository, times(1)).findById(id);
    }

    @Test
    void updatePrivacyPolicy_ShouldReturnUpdatedPolicy_WhenPolicyExists() {
        when(privacyPolicyRepository.findById(id)).thenReturn(Optional.of(privacyPolicy));
        when(privacyPolicyRepository.save(any(PrivacyPolicy.class))).thenReturn(privacyPolicy);

        PrivacyPolicy updatedPolicy = new PrivacyPolicy();
        updatedPolicy.setContent("Updated Content");

        PrivacyPolicy result = privacyPolicyService.updatePrivacyPolicy(id, updatedPolicy);

        assertNotNull(result);
        assertEquals("Updated Content", result.getContent());
        verify(privacyPolicyRepository, times(1)).findById(id);
        verify(privacyPolicyRepository, times(1)).save(privacyPolicy);
    }

    @Test
    void updatePrivacyPolicy_ShouldThrowException_WhenPolicyDoesNotExist() {
        when(privacyPolicyRepository.findById(id)).thenReturn(Optional.empty());

        PrivacyPolicy updatedPolicy = new PrivacyPolicy();
        updatedPolicy.setContent("Updated Content");

        assertThrows(PrivacyPolicyNotFoundException.class, () -> privacyPolicyService.updatePrivacyPolicy(id, updatedPolicy));
        verify(privacyPolicyRepository, times(1)).findById(id);
    }

    @Test
    void deletePrivacyPolicy_ShouldDeletePolicy_WhenPolicyExists() {
        when(privacyPolicyRepository.existsById(id)).thenReturn(true);

        privacyPolicyService.deletePrivacyPolicy(id);

        verify(privacyPolicyRepository, times(1)).existsById(id);
        verify(privacyPolicyRepository, times(1)).deleteById(id);
    }

    @Test
    void deletePrivacyPolicy_ShouldThrowException_WhenPolicyDoesNotExist() {
        when(privacyPolicyRepository.existsById(id)).thenReturn(false);

        assertThrows(PrivacyPolicyNotFoundException.class, () -> privacyPolicyService.deletePrivacyPolicy(id));
        verify(privacyPolicyRepository, times(1)).existsById(id);
    }
}
