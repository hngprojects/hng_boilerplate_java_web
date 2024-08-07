package hng_java_boilerplate.user.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import hng_java_boilerplate.user.dto.response.ForgotPasswordResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;

@Component
public class UserUtils {

    @Value("${jwt.secret}")
    private String secret;

    public static String PASSWORD_RESET = "Password Reset";

    public String generateResetLink(String emailAddress) {
        String token = JWT.create().withIssuedAt(Instant.now()).withExpiresAt(Instant.now().plusSeconds(86000L))
                .withClaim("email", emailAddress)
                .sign(Algorithm.HMAC512(secret.getBytes()));
        return "http://localhost:3000/reset-password?token=" + token;
    }

    public static ForgotPasswordResponse validateEmailNotEmpty(String email) {
        if (email == null || email.isEmpty()) {
            return new ForgotPasswordResponse("400", "Provide a valid email", new HashMap<>());
        }
        return null;
    }

}
