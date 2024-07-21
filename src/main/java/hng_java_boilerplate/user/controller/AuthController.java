package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.jwt.JwtService;
import hng_java_boilerplate.user.dto.LogInDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.response.LogInResponse;
import hng_java_boilerplate.user.response.UnsuccesfulResponse;
import hng_java_boilerplate.user.service.MyUserDetailsService;
import hng_java_boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    private final MyUserDetailsService myUserDetailsService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;



    @PostMapping("/login")
    public ResponseEntity<?> LogIn(@Validated @RequestBody LogInDto logInDto){
        try {
            User user = userService.LogIn(logInDto);
            String jwt = jwtService.generateToken(myUserDetailsService.loadUserByUsername(logInDto.getEmail()));
            LogInResponse logInResponse = new LogInResponse(jwt);

            return new ResponseEntity<>(logInResponse, HttpStatus.OK);
        } catch (Exception e){
            UnsuccesfulResponse response = new UnsuccesfulResponse("Authentication failed", "Invalid email or password.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
