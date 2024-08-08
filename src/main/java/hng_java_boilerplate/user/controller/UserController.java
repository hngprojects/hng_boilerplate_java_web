package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.user.dto.response.Response;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id, Authentication authentication) {
        try {
            Response<?> response = userService.getUserById(id, authentication);
            return ResponseEntity.status(response.getStatus().equals("200") ? 200 : 401).body(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Response.builder().status("404").message(e.getMessage()).build());
        }
    }



}
