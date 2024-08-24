package hng_java_boilerplate.user.service;

import hng_java_boilerplate.user.dto.request.*;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    GetUserDto getUserWithDetails(String userId);
    ResponseEntity<ApiResponse<ResponseData>> registerUser(SignupDto signupDto);
    ResponseEntity<String> verifyOtp(String email, String token, HttpServletRequest request);
    User getLoggedInUser();
    ResponseEntity<ApiResponse<ResponseData>> loginUser(LoginDto loginDto);
    User save(User user);
    User findUser(String id);
    void forgotPassword(EmailSenderDto passwordDto, HttpServletRequest request);
    ResponseEntity<String> resetPassword(String token, ResetPasswordDto passwordDto);
    void requestToken(EmailSenderDto emailSenderDto, HttpServletRequest request);
    void sendMagicLink(String email, HttpServletRequest request);
}
