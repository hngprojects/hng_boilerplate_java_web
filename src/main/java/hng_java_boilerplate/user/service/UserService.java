package hng_java_boilerplate.user.service;

import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import javax.crypto.BadPaddingException;

public interface UserService {
    GetUserDto getUserWithDetails(String userId) throws BadPaddingException;
    ResponseEntity<ApiResponse> registerUser(SignupDto signupDto);
}
