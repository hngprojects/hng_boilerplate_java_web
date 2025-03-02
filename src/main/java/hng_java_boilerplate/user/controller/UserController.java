package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.user.dto.request.DeleteUserRequest;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.dto.response.MembersResponse;
import hng_java_boilerplate.user.dto.response.Response;
import hng_java_boilerplate.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name="Users", description="Controller for managing users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieve user details using the user id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "When there is no user with the id provided",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "when the user is not authenticated")
    })
    @Operation(summary = "Get a user details", description = "Retrieve a user detail using the user id")
    public ResponseEntity<?> getUserDetails(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserWithDetails(userId));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully retrieves all user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(responseCode = "401", description = "when the user is not authenticated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "when the user is not an admin user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @Operation(summary = "Get a list of all users", description = "Admin user retrieves a list of all users")
    @GetMapping(value = "/members", produces = "application/json")
    public ResponseEntity<?> getAllMembers(@RequestParam int page, Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().message("Unauthorized").status_code("401").build());

        List<MembersResponse> allUsers = userService.getAllUsers(page, authentication);
        Response<?> response = Response.builder().message("Users List Successfully Fetched").status_code("200").data(allUsers).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deletes a user by their email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(responseCode = "401", description = "when the user is not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "when the user is not an admin user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "when the user with the email doesn't exist")
    })
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @Operation(summary = "delete a user", description = "Delete a user using the user email")
    public ResponseEntity<?> deleteUserByEmail(@Valid @RequestBody DeleteUserRequest request, Authentication authentication) {
        Response<?> response = userService.deleteUserByEmail(request, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/me/{id}")
    public Response<?> getUserById(@PathVariable String id, Authentication authentication) {
        return userService.getUserById(id, authentication);
    }
}
