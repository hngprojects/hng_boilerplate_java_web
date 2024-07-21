package hng_java_boilerplate.squeeze.service;

import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.exceptions.DuplicateEmailException;
import hng_java_boilerplate.squeeze.repository.SqueezeRequestRepository;
import hng_java_boilerplate.squeeze.service.SqueezeRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SqueezeRequestServiceTest {

    @Mock
    private SqueezeRequestRepository repository;

    @InjectMocks
    private SqueezeRequestService service;

    private SqueezeRequest validRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a valid SqueezeRequest object
        validRequest = SqueezeRequest.builder()
                .email("user@example.com")
                .first_name("John")
                .last_name("Doe")
                .phone("08098761234")
                .location("Lagos, Nigeria")
                .job_title("Software Engineer")
                .company("X-Corp")
                .interests(new ArrayList<>(List.of("Web Development", "Cloud Computing")))
                .referral_source("LinkedIn")
                .build();
    }

    @Test
    public void testSaveSqueezeRequest_Success() throws DuplicateEmailException {
        when(repository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(repository.save(any(SqueezeRequest.class))).thenReturn(validRequest);

        SqueezeRequest savedRequest = service.saveSqueezeRequest(validRequest);

        assertNotNull(savedRequest);
        assertEquals(validRequest.getEmail(), savedRequest.getEmail());
        assertEquals(validRequest.getFirst_name(), savedRequest.getFirst_name());
    }

    @Test
    public void testSaveSqueezeRequest_DuplicateEmail() {
        when(repository.existsByEmail(validRequest.getEmail())).thenReturn(true);

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> {
            service.saveSqueezeRequest(validRequest);
        });

        assertEquals("Email address already exists", exception.getMessage());
    }
}

