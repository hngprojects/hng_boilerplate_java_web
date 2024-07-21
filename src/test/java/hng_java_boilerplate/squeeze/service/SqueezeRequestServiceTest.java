package hng_java_boilerplate.squeeze.service;

import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.repository.SqueezeRequestRepository;
import hng_java_boilerplate.squeeze.service.EmailService;
import hng_java_boilerplate.squeeze.service.SqueezeRequestService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class SqueezeRequestServiceTest {

    @InjectMocks
    private SqueezeRequestService service;

    @Mock
    private SqueezeRequestRepository repository;

    @Mock
    private EmailService emailService;

    public SqueezeRequestServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveSqueezeRequest() {
        SqueezeRequest request = new SqueezeRequest();
        request.setEmail("user@example.com");

        when(repository.save(request)).thenReturn(request);

        SqueezeRequest savedRequest = service.saveSqueezeRequest(request);

        assertNotNull(savedRequest);
        verify(repository, times(1)).save(request);
        verify(emailService, times(1)).sendTemplateEmail("user@example.com");
    }
}
