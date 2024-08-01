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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SqueezeRequestServiceTest {

    @Mock
    private SqueezeRequestRepository repository;

    @InjectMocks
    private SqueezeRequestService service;

    private SqueezeRequest validRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

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

        assertEquals("EmailTemplates address already exists", exception.getMessage());
    }

    @Test
    void testUpdateSqueezeRequest_Success() {
        when(repository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(validRequest));
        when(repository.save(any(SqueezeRequest.class))).thenReturn(validRequest);

        SqueezeRequest updatedRequest = service.updateSqueezeRequest(validRequest);

        assertNotNull(updatedRequest);
        assertEquals(validRequest.getEmail(), updatedRequest.getEmail());
        assertTrue(updatedRequest.isUpdated());
        verify(repository, times(1)).save(any(SqueezeRequest.class));
    }

    @Test
    void testUpdateSqueezeRequest_RecordNotFound() {
        when(repository.findByEmail(validRequest.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            service.updateSqueezeRequest(validRequest);
        });

        assertEquals("No squeeze page record exists for the provided request body", exception.getMessage());
    }

    @Test
    void testUpdateSqueezeRequest_AlreadyUpdated() {
        validRequest.setUpdated(true);
        when(repository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(validRequest));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            service.updateSqueezeRequest(validRequest);
        });

        assertEquals("The squeeze page record can only be updated once.", exception.getMessage());
    }
}

