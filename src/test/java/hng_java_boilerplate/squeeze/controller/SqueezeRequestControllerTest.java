package hng_java_boilerplate.squeeze.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.product.errorhandler.ProductErrorHandler;
import hng_java_boilerplate.squeeze.dto.ResponseMessageDto;
import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.exceptions.DuplicateEmailException;
import hng_java_boilerplate.squeeze.service.SqueezeRequestService;
import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SqueezeRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SqueezeRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SqueezeRequestService service;

    @MockBean
    private EmailProducerService emailProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    private SqueezeRequest validRequest;

    @MockBean
    private ProductErrorHandler productErrorHandler;

    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        validRequest = SqueezeRequest.builder()
                .email("testemail@email.com")
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
    public void testHandleSqueezeRequest_Success() throws Exception {
        when(service.saveSqueezeRequest(any(SqueezeRequest.class))).thenReturn(validRequest);

        mockMvc.perform(post("/api/v1/squeeze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("You are all signed up!"));
    }

    @Test
    public void testHandleSqueezeRequest_DuplicateEmail() throws Exception {
        when(service.saveSqueezeRequest(any(SqueezeRequest.class))).thenThrow(new DuplicateEmailException("Email address already exists"));

        mockMvc.perform(post("/api/v1/squeeze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email address already exists"))
                .andExpect(jsonPath("$.status_code").value(409));
    }

}
