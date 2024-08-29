package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.user.dto.request.DeleteUserRequest;
import hng_java_boilerplate.user.dto.response.MembersResponse;
import hng_java_boilerplate.user.dto.response.Response;
import hng_java_boilerplate.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
        if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().message("Unauthorized").status_code("401").build());

        List<MembersResponse> allUsers = userService.getAllUsers(page, authentication);
        Response<?> response = Response.builder().message("Users List Successfully Fetched").status_code("200").data(allUsers).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserByEmail(@Valid @RequestBody DeleteUserRequest request, Authentication authentication) {
        Response<?> response = userService.deleteUserByEmail(request, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/me/{id}")
    public Response<?> getUserById(@PathVariable String id, Authentication authentication) {
        return userService.getUserById(id, authentication);
    }


}
