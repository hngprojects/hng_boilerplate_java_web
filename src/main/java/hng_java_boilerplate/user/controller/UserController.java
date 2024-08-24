package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.user.dto.response.MembersResponse;
import hng_java_boilerplate.user.dto.response.Response;
import hng_java_boilerplate.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping(value = "/members", produces = "application/json")
    public ResponseEntity<?> getAllMembers(@RequestParam int page, Authentication authentication) {
        List<MembersResponse> allUsers = userService.getAllUsers(page, authentication);
        Response<?> response = Response.builder().message("Users List Successfully Fetched").status_code("200").data(allUsers).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
