package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.organisation.dto.responses.MembersResponse;
import hng_java_boilerplate.organisation.dto.responses.Response;
import hng_java_boilerplate.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/members", produces = "application/json")
    public ResponseEntity<?> getAllMembers(@RequestParam int page, Authentication authentication) {
        List<MembersResponse> members = userService.getAllUsers(authentication, page);
        var res = Response.builder().message("good").status(true).data(members).build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


}
