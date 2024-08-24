package hng_java_boilerplate.user.service;

import hng_java_boilerplate.user.dto.request.EmailSenderDto;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.dto.request.LoginDto;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.MembersResponse;
import hng_java_boilerplate.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface UserService {
    GetUserDto getUserWithDetails(String userId);
    ResponseEntity<ApiResponse> registerUser(SignupDto signupDto);
    ResponseEntity<String> verifyOtp(String email, String token, HttpServletRequest request);
    User getLoggedInUser();
    ResponseEntity<ApiResponse> loginUser(LoginDto loginDto);
    User save(User user);
    User findUser(String id);

    void forgotPassword(EmailSenderDto passwordDto, HttpServletRequest request);

    List<MembersResponse> getAllUsers(int page, Authentication authentication);

}
