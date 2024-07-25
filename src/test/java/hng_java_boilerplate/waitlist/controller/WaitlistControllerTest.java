package hng_java_boilerplate.waitlist.controller;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.product.errorhandler.ProductErrorHandler;
import hng_java_boilerplate.waitlist.entity.Waitlist;
import hng_java_boilerplate.waitlist.service.WaitlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WaitlistController.class)
@WithMockUser
public class WaitlistControllerTest {

    @MockBean
    private WaitlistService waitlistService;

    @MockBean
    private EmailProducerService emailProducerService;

    @MockBean
    private ProductErrorHandler productErrorHandler;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new WaitlistController(waitlistService, emailProducerService)).build();
    }

    @Test
    void createWaitlist() throws Exception {
        // Mock the Waitlist object
        Waitlist waitlist = new Waitlist(UUID.randomUUID(), "Test User", "test@example.com");

        // Mock the service method
        when(waitlistService.saveWaitlist(any(Waitlist.class))).thenReturn(waitlist);
        doNothing().when(emailProducerService).sendEmailMessage(any(String.class), any(String.class), any(String.class));

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/v1/waitlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"fullName\":\"Test User\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("You are all signed up!"));

        // Verify that the expected methods were called
        verify(waitlistService).saveWaitlist(any(Waitlist.class));
        verify(emailProducerService).sendEmailMessage("test@example.com", "Confirmation Email", "You are all signed up!");
    }
}
