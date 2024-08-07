package hng_java_boilerplate.user.service;

import hng_java_boilerplate.user.dto.request.ForgotPasswordRequest;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.dto.request.LoginDto;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.ForgotPasswordResponse;
import hng_java_boilerplate.user.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import javax.crypto.BadPaddingException;

public interface UserService {
    GetUserDto getUserWithDetails(String userId);
    ResponseEntity<ApiResponse> registerUser(SignupDto signupDto);
    User getLoggedInUser();

    ResponseEntity<ApiResponse> loginUser(LoginDto loginDto);
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) throws MessagingException;
}
