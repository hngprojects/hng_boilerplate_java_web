package hng_java_boilerplate.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.profile.repository.ProfileRepository;
import hng_java_boilerplate.user.dto.request.OAuthDto;
import hng_java_boilerplate.user.dto.response.*;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.function.Function;

@Component
public class GoogleJwtUtils {

    private UserRepository userRepository;
    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtils utils;
    private ProfileRepository profileRepository;

    @Autowired
    public GoogleJwtUtils(UserRepository userRepository, @Lazy UserServiceImpl userService, PasswordEncoder passwordEncoder, JwtUtils utils, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;;
        this.profileRepository = profileRepository;
    }



    private final Function<OAuthDto, OAuthResponse> getUserFromIdToken = (oAuthDto) -> {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .build();

        GoogleIdToken token = null;
        try {
            token = verifier.verify(oAuthDto.getIdToken());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if (token != null) {
            Payload payload = token.getPayload();
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String pictureUrl = (String) payload.get("picture");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            return OAuthResponse
                    .builder()
                    .email(email)
                    .first_name(givenName)
                    .last_name(familyName)
                    .password("GOOGLELOGIN1")
                    .img_url(pictureUrl)
                    .is_active(emailVerified)
                    .build();
        }
        return null;
    };

    public Function<OAuthResponse, OAuthUserResponse> saveOauthUser = userDto -> {
        if (userRepository.existsByEmail(userDto.getEmail())){
            UserDetails userDetails = userService.loadUserByUsername(userDto.getEmail());
             return getAuthUserResponse(userDto ,(User)userDetails);
        }
        else{
            User user = new User();
            user.setName(userDto.getFirst_name() + " " + userDto.getLast_name());
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setUserRole(Role.ROLE_USER);
            user.setIsEnabled(userDto.is_active());

            Profile profile = populateProfile(userDto);

            user.setProfile(profile);
            User savedUser = userRepository.save(user);

            return getAuthUserResponse(userDto, savedUser);
        }
    };

    public OAuthUserResponse getAuthUserResponse(OAuthResponse authDto, User user){
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String token = utils.createJwt.apply(userDetails);

        OAuthUserResponse oAuthUserResponse = new OAuthUserResponse();
        oAuthUserResponse.setId(user.getId());
        oAuthUserResponse.setEmail(user.getEmail());
        oAuthUserResponse.setFirst_name(authDto.getFirst_name());
        oAuthUserResponse.setLast_name(authDto.getLast_name());
        oAuthUserResponse.setFullname(authDto.getFirst_name() + " " + authDto.getLast_name());
        oAuthUserResponse.setRole(user.getUserRole().name());
        oAuthUserResponse.setAccess_token(token);
        return oAuthUserResponse;
    }

    OAuthLastUserResponse oAuthLastUserResponse(UserOAuthDetails userOAuthDetails){
        OAuthLastUserResponse oAuthLastUserResponse = new OAuthLastUserResponse();
        oAuthLastUserResponse.setUser(userOAuthDetails);
        return oAuthLastUserResponse;
    }

    UserOAuthDetails userOAuthDetailsResponse(OAuthUserResponse oAuthUserResponse){
        UserOAuthDetails userOAuthDetails = new UserOAuthDetails();
        userOAuthDetails.setId(oAuthUserResponse.getId());
        userOAuthDetails.setEmail(oAuthUserResponse.getEmail());
        userOAuthDetails.setFirst_name(oAuthUserResponse.getFirst_name());
        userOAuthDetails.setLast_name(oAuthUserResponse.getLast_name());
        userOAuthDetails.setFullname(oAuthUserResponse.getFullname());
        userOAuthDetails.setRole(oAuthUserResponse.getRole());
        return userOAuthDetails;
    }

    private Profile populateProfile(OAuthResponse user){
        Profile profile = new Profile();
        profile.setFirstName(user.getFirst_name());
        profile.setLastName(user.getLast_name());
        profile.setAvatarUrl(user.getImg_url());

        return profileRepository.save(profile);
    }

    public OAuthBaseResponse googleOauthUserJWT(OAuthDto oAuthDto){
        OAuthResponse user =  getUserFromIdToken.apply(oAuthDto);
        OAuthUserResponse data = saveOauthUser.apply(user);
        UserOAuthDetails userOAuthDetails = userOAuthDetailsResponse(data);
        OAuthLastUserResponse lastData = oAuthLastUserResponse(userOAuthDetails);
        return new OAuthBaseResponse(HttpStatus.OK.value(), "User Created Successful!", data.getAccess_token(), lastData);
    }
}