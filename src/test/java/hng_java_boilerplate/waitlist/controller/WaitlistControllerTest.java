package hng_java_boilerplate.waitlist.controller;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.util.JwtUtils;
import hng_java_boilerplate.waitlist.entity.Waitlist;
import hng_java_boilerplate.waitlist.service.WaitlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WaitlistController.class)
public class WaitlistControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private WaitlistService waitlistService;

    @MockBean
    private EmailProducerService emailProducerService;

    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createWaitlist() throws Exception {
        // Mock the Waitlist object
        Waitlist waitlist = new Waitlist(UUID.randomUUID(), "Test User", "test@example.com", LocalDateTime.now());

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

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWaitlistUsers() throws Exception {
        // Mock pageable and page
        Pageable pageable = PageRequest.of(0, 10);
        Waitlist waitlistUser = new Waitlist(UUID.randomUUID(), "Test User", "test@example.com", LocalDateTime.now());
        Page<Waitlist> waitlistPage = new PageImpl<>(Collections.singletonList(waitlistUser), pageable, 1);

        // Mock the service method
        when(waitlistService.getWaitlistUsers(pageable)).thenReturn(waitlistPage);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/v1/waitlist")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users[0].email").value("test@example.com"))
                .andExpect(jsonPath("$.users[0].fullName").value("Test User"))
                .andExpect(jsonPath("$.users[0].signupDate").isNotEmpty())
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("Retrieved waitlist users successfully"));

        // Verify that the expected method was called
        verify(waitlistService).getWaitlistUsers(pageable);
    }
}