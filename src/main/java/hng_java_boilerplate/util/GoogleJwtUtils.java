package hng_java_boilerplate.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.function.Function;


@Component
public class GoogleJwtUtils {


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;
    private UserRepository userRepository;
    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtils utils;
    @Autowired
    public GoogleJwtUtils(UserRepository userRepository, @Lazy UserServiceImpl userService, PasswordEncoder passwordEncoder, JwtUtils utils) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;
    }

    private final Function<String, SignupDto> getUserFromIdToken = (token)-> {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(token);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            return SignupDto
                    .builder()
                    .email(email)
                    .firstName(givenName)
                    .lastName(familyName)
                    .password("GOOGLELOGIN1")
                    .build();
        }
        return null;
    };


    public Function<SignupDto, String> saveOauthUser = userDto -> {
        if (userRepository.existsByEmail(userDto.getEmail())){
            UserDetails userDetails = userService.loadUserByUsername(userDto.getEmail());
            return utils.createJwt.apply(userDetails);
        }
        else{
            User user = new ObjectMapper().convertValue(userDto, User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUserRole(Role.ROLE_USER);
            user = userRepository.save(user);
            return utils.createJwt.apply(userService.loadUserByUsername(user.getUsername()));
        }
    };

    public String googleOauthUserJWT(String token){
        SignupDto user =  getUserFromIdToken.apply(token);
        return saveOauthUser.apply(user);
    }
}
