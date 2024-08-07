package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.user.dto.request.ForgotPasswordRequest;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name="Users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserWithDetails(userId));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        var response = userService.forgotPassword(request);
        if (response.getMessage().equals("Email does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (response.getMessage().equals("Provide a valid email")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
