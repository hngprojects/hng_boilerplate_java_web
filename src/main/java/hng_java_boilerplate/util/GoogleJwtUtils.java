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
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.dto.response.UserResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    public Function<SignupDto, ResponseData> saveOauthUser = userDto -> {
        if (userRepository.existsByEmail(userDto.getEmail())){
            UserDetails userDetails = userService.loadUserByUsername(userDto.getEmail());
            String token = utils.createJwt.apply(userDetails);

            UserResponse userResponse = userService.getUserResponse((User)userDetails);
            return new ResponseData(token, userResponse);

        }
        else{
            User user = new User();
            user.setName(userDto.getFirstName() + " " + userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setUserRole(Role.ROLE_USER);
            User savedUser = userRepository.save(user);

            String token = utils.createJwt.apply(userService.loadUserByUsername(savedUser.getUsername()));

            UserResponse userResponse = userService.getUserResponse(savedUser);
            return new ResponseData(token, userResponse);

        }
    };

    public ResponseEntity<ApiResponse> googleOauthUserJWT(String token){
        SignupDto user =  getUserFromIdToken.apply(token);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Login Successful!", saveOauthUser.apply(user)), HttpStatus.OK);
    }
}
