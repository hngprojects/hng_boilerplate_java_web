package hng_java_boilerplate.authentication.user.service;

import hng_java_boilerplate.authentication.user.entity.User;
import hng_java_boilerplate.authentication.user.dto.request.GetUserDto;
import hng_java_boilerplate.authentication.user.dto.request.LoginDto;
import hng_java_boilerplate.authentication.user.dto.request.SignupDto;
import hng_java_boilerplate.authentication.user.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    GetUserDto getUserWithDetails(String userId);
    ResponseEntity<ApiResponse> registerUser(SignupDto signupDto);
    ResponseEntity<String> verifyOtp(String email, String token, HttpServletRequest request);
    User getLoggedInUser();
    ResponseEntity<ApiResponse> loginUser(LoginDto loginDto);
    User save(User user);

    User findUser(String id);
}
