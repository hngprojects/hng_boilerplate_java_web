package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(userService.getUserWithDetails(userId));
        } catch (BadPaddingException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("user not found");
        }
    }
}
