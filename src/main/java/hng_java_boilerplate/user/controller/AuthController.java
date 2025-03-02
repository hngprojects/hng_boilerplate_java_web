package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.user.dto.request.*;
import hng_java_boilerplate.user.dto.response.OAuthBaseResponse;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.util.FacebookJwtUtils;
import hng_java_boilerplate.util.GoogleJwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name="Authentication", description = "Controller that handles user authentication")
public class AuthController {

    private final UserService userService;
    private final FacebookJwtUtils facebookJwtUtils;
    private final GoogleJwtUtils googleJwtUtils;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user is successfully registered",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = hng_java_boilerplate.user.dto.response.ApiResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "When a user already exists with same email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "422", description = "when user do not provide all required request data")
    })
    @Operation(summary = "Register new user", description = "Registers a new user with provided credentials")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupDto signupDto){
        return userService.registerUser(signupDto);
    }


    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user is successfully logged in",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = hng_java_boilerplate.user.dto.response.ApiResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "When a user provides invalid login credentials",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "422", description = "when user do not provide all required request data")
    })
    @Operation(summary = "Register new user", description = "Registers a new user with provided credentials")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        return userService.loginUser(loginDto);
    }


    @PostMapping("/facebook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user authentication with facebook success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OAuthBaseResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "invalid user request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @Operation(summary = "User authentication using facebook", description = "User can register and login using facebook")
    public ResponseEntity<OAuthBaseResponse> handleFacebookAuth(@RequestBody FacebookDto payload) {
        try {
            return new ResponseEntity<>(facebookJwtUtils.facebookOauthUserJWT(payload), HttpStatus.CREATED);
        } catch (UnAuthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/google")
    @ApiResponse(responseCode = "200", description = "user authentication with google success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OAuthBaseResponse.class))
    )
    @Operation(summary = "User authentication using google", description = "User can register and login using google")
    public ResponseEntity<OAuthBaseResponse> handleGoogleAuth(@RequestBody GoogleOAuthDto payload) {
        try {
            return ResponseEntity.ok(googleJwtUtils.googleOauthUserJWT(payload));
        } catch (UnAuthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @ApiResponse(responseCode = "200", description = "successfully sends forgot password email to user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    )
    @PostMapping("/forgot-password")
    @Operation(summary = "sends forgot password to user")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailSenderDto passwordDto, HttpServletRequest request){
        userService.forgotPassword(passwordDto, request);
        return new ResponseEntity<>("Forgot password email sent successfully", HttpStatus.OK);
    }


    @ApiResponse(responseCode = "200", description = "user reset password success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    )
    @Operation(summary = "User resets password")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordDto passwordDto) {
        return userService.resetPassword(token, passwordDto);
    }


    @PostMapping("/request/token")
    @ApiResponse(responseCode = "200", description = "user authentication with google success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    )
    @Operation(summary = "User verify email", description = "sends a token to user email for verification")
    public ResponseEntity<?> requestToken(@RequestBody EmailSenderDto emailSenderDto, HttpServletRequest request){
        userService.requestToken(emailSenderDto, request);
        return new ResponseEntity<>("Verification email sent successfully", HttpStatus.OK);
    }


    @PostMapping("/verify-otp")
    @ApiResponse(responseCode = "200", description = "user verifies otp token success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    )
    @Operation(summary = "user verify otp token", description = "verifies the otp token sent to the user email")
    public ResponseEntity<String> verifyOtp(@RequestBody VerificationTokenDto verificationTokenDto, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.verifyOtp(email, verificationTokenDto.getToken(), request);
    }

    @PostMapping("/magic-link")
    @ApiResponse(responseCode = "200", description = "magic link sent success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    )
    @Operation(summary = "send magic link to user", description = "sends a login link the user can use to log in to the user email")
    public ResponseEntity<String> sendMagicLink(@RequestBody MagicLinkRequest magicLinkRequest, HttpServletRequest request) {
        userService.sendMagicLink(magicLinkRequest.getEmail(), request);
        return new ResponseEntity<>("Magic link sent successfully! Go to your email to login", HttpStatus.OK);
    }

    @PostMapping("/magic-link/login")
    @ApiResponse(responseCode = "200", description = "login success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = hng_java_boilerplate.user.dto.response.ApiResponse.class))
    )
    @Operation(summary = "logs in a user", description = "logs in a user using the magic link sent to their email")
    public  ResponseEntity<?> magicLinkLogin(@RequestParam("token") String token){
        return userService.magicLinkLogin(token);
    }
}