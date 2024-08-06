package hng_java_boilerplate.user.crudOperations;

import hng_java_boilerplate.organisation.dto.responses.MembersResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.InvalidPageNumberException;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;


    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user-123");
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setPhoneNumber("1234567890");
        user.setStatus("active");
        user.setCreatedAt(LocalDateTime.now());

    }

    @Test
    void getAllUsers_shouldReturnPaginatedUsers_whenUsersExist() {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findAll()).thenReturn(userList);

        List<MembersResponse> users = userService.getAllUsers(1, authentication);

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getFullName());
        assertEquals("johndoe@example.com", users.get(0).getEmail());
        assertEquals("1234567890", users.get(0).getPhone());
        assertEquals("active", users.get(0).getStatus());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_shouldThrowException_whenInvalidPageRequested() {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(authentication.getPrincipal()).thenReturn(user);

        InvalidPageNumberException exception = assertThrows(
                InvalidPageNumberException.class,
                () -> userService.getAllUsers(2, authentication)
        );

        assertEquals("Invalid page number requested: 2", exception.getMessage());
    }
}



