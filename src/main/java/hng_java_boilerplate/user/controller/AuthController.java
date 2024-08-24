package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.user.dto.request.*;
import hng_java_boilerplate.user.dto.response.OAuthBaseResponse;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.util.FacebookJwtUtils;
import hng_java_boilerplate.util.GoogleJwtUtils;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name="Authentication")
public class AuthController {

    private final UserService userService;
    private final FacebookJwtUtils facebookJwtUtils;
    private final GoogleJwtUtils googleJwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupDto signupDto){
        return userService.registerUser(signupDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        return userService.loginUser(loginDto);
    }

    @PostMapping("/facebook")
    public ResponseEntity<OAuthBaseResponse> handleFacebookAuth(@RequestBody FacebookDto payload) {
        try {
            return new ResponseEntity<>(facebookJwtUtils.facebookOauthUserJWT(payload), HttpStatus.CREATED);
        } catch (UnAuthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/google")
    public ResponseEntity<OAuthBaseResponse> handleGoogleAuth(@RequestBody GoogleOAuthDto payload) {
        try {
            return ResponseEntity.ok(googleJwtUtils.googleOauthUserJWT(payload));
        } catch (UnAuthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailSenderDto passwordDto, HttpServletRequest request){
        userService.forgotPassword(passwordDto, request);
        return new ResponseEntity<>("Forgot password email sent successfully", HttpStatus.OK);
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody ResetPasswordDto passwordDto) {
        return userService.resetPassword(token, passwordDto);
    }

    @PostMapping("/request/token")
    public ResponseEntity<?> requestToken(@RequestBody EmailSenderDto emailSenderDto, HttpServletRequest request){
        userService.requestToken(emailSenderDto, request);
        return new ResponseEntity<>("Verification email sent successfully", HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerificationTokenDto verificationTokenDto, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.verifyOtp(email, verificationTokenDto.getToken(), request);
    }

    @PostMapping("/magic-link")
    public ResponseEntity<String> sendMagicLink(@RequestBody MagicLinkRequest magicLinkRequest, HttpServletRequest request) {
        userService.sendMagicLink(magicLinkRequest.getEmail(), request);
        return new ResponseEntity<>("Magic link sent successfully! Go to your email to login", HttpStatus.OK);
    }
}