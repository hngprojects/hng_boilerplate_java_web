package hng_java_boilerplate.user.crud_operations_test;

import hng_java_boilerplate.user.dto.request.DeleteUserRequest;
import hng_java_boilerplate.user.dto.response.MembersResponse;
import hng_java_boilerplate.user.dto.response.Response;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteUserTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;


    @Test
    void deleteUserSuccessfullyTest() {
        String email = "johndoe@example.com";
        DeleteUserRequest request = new DeleteUserRequest();
        request.setEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        Response<?> response = userService.deleteUserByEmail(request, authentication);

        assertEquals("success", response.getStatus_code());
        assertEquals("The account has been successfully deleted.", response.getMessage());

        verify(userRepository, times(1)).deleteByEmail(email);
    }

    @Test
    void deleteUserNotFoundTest() {
        String email = "johndoe@example.com";
        DeleteUserRequest request = new DeleteUserRequest();
        request.setEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        Response<?> response = userService.deleteUserByEmail(request, authentication);

        assertEquals("404", response.getStatus_code());
        assertEquals("User not found with email: " + email, response.getMessage());

        verify(userRepository, never()).deleteByEmail(email);
    }

    @Test
    void deleteUserBadRequestTest() {
        DeleteUserRequest request = new DeleteUserRequest();
        request.setEmail("email@gmail.com"); 
        
        Response<?> response = userService.deleteUserByEmail(request, authentication);

        assertEquals("400", response.getStatus_code());
        assertEquals("Bad Request. The email field is required.", response.getMessage());

        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).deleteByEmail(anyString());
    }
}
