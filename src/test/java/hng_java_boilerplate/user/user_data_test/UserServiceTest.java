package hng_java_boilerplate.user.user_data_test;

import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId("id");
        user.setUserRole(Role.ROLE_USER);
        user.setName("Name");
        user.setPassword("password123@");
        user.setEmail("omoyeni76@gmail.com");
        user.setCreatedAt(LocalDateTime.now());
    }
    @Test
    public void getSingleUserTest() {
        when(authentication.getName()).thenReturn("omoyeni76@gmail.com");

        when(userRepository.existsByEmail("omoyeni76@gmail.com")).thenReturn(true);
        when(userRepository.findById("id")).thenReturn(Optional.of(user));
        var response = userService.getUserById("id", authentication);
        assertNotNull(response);
    }

}
