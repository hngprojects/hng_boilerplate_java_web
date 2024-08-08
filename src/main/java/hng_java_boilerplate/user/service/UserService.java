package hng_java_boilerplate.user.service;

import hng_java_boilerplate.user.dto.request.EmailSenderDto;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.dto.request.LoginDto;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import javax.crypto.BadPaddingException;

public interface UserService {
    GetUserDto getUserWithDetails(String userId);
    ResponseEntity<ApiResponse> registerUser(SignupDto signupDto);
    User getLoggedInUser();
    void requestToken(EmailSenderDto emailSenderDto, HttpServletRequest request);
   ResponseEntity<ApiResponse> loginUser(LoginDto loginDto);
    User save(User user);
}
