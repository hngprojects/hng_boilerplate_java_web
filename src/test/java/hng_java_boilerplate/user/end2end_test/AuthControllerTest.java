package hng_java_boilerplate.user.end2end_test;


import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    void registerUser_Success() throws Exception {
        SignupDto signupDto = new SignupDto();
        signupDto.setFirstName("John");
        signupDto.setLastName("Doe");
        signupDto.setEmail("john.doe@example.com");
        signupDto.setPassword("Password123");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registration Successful!"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.user.id").isNotEmpty())
                .andExpect(jsonPath("$.data.user.first_name").value("John"))
                .andExpect(jsonPath("$.data.user.last_name").value("Doe"))
                .andExpect(jsonPath("$.data.user.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.data.user.created_at").isNotEmpty());
    }


    @Test
    void registerUser_InvalidPassword() throws Exception {
        SignupDto signupDto = new SignupDto();
        signupDto.setFirstName("John");
        signupDto.setLastName("Doe");
        signupDto.setEmail("john.doe@example.com");
        signupDto.setPassword("pass"); // Invalid password

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("password: Password must be at least 8 characters long and contain alphanumeric characters; "));
    }
}
