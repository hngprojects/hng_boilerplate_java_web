package hng_java_boilerplate.user.passwordReset;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.user.dto.request.ForgotPasswordRequest;
import hng_java_boilerplate.user.dto.response.ForgotPasswordResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForgotPasswordTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailProducerService producer;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("id");
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        user.setName("name");
    }

    @Test
    void testForgotPassword() throws MessagingException {
        ForgotPasswordRequest request = new ForgotPasswordRequest("test@example.com");
        ForgotPasswordResponse response = userService.forgotPassword(request);
        assertNotNull(response);
    }

}
