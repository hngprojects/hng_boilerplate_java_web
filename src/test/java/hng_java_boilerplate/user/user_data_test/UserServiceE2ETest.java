package hng_java_boilerplate.user.user_data_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;


    private String id;

    private final String email = "testingtesting@gmail.com";

    @BeforeEach
    public void setUp() {
        SignupDto dto = new SignupDto();
        dto.setEmail(email);
        dto.setLastName("lastname");
        dto.setFirstName("firstname");
        dto.setPassword("password123£");
        var response = userService.registerUser(dto);

        id = Objects.requireNonNull(response.getBody()).getData().getUser().getId();
    }


    @Test
    public void getUserById_shouldReturn403_whenUserIsNotAuthenticated() throws Exception {
        String userId = id;

        mockMvc.perform(get("/users/me/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


}
