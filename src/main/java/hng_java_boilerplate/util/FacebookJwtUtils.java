package hng_java_boilerplate.util;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.profile.repository.ProfileRepository;
import hng_java_boilerplate.user.dto.request.OAuthDto;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.OAuthResponse;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.dto.response.UserResponse;
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

import java.util.function.Function;

@Component
public class FacebookJwtUtils {

    private UserRepository userRepository;
    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtils utils;
    private ProfileRepository profileRepository;

    @Autowired
    public FacebookJwtUtils(UserRepository userRepository, @Lazy UserServiceImpl userService, PasswordEncoder passwordEncoder, JwtUtils utils, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;;
        this.profileRepository = profileRepository;
    }


    private final Function<String, OAuthResponse> getUserFromFacebookToken = (accessToken) -> {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);

        com.restfb.types.User user = null;
        try {
            user = facebookClient.fetchObject("me", com.restfb.types.User.class,
                    com.restfb.Parameter.with("fields", "id,email,first_name,last_name,picture"));
        } catch (RuntimeException e) {
            throw new BadRequestException("Bad Request");
        }

        if (user != null) {
            String providerAccountId = user.getId();
            String email = user.getEmail();
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String pictureUrl = user.getPicture().getUrl();

            return OAuthResponse
                    .builder()
                    .email(email)
                    .first_name(firstName)
                    .last_name(lastName)
                    .password("FACEBOOKLOGIN1")
                    .img_url(pictureUrl)
                    .is_active(true)
                    .build();
        }
        return null;
    };

    public Function<OAuthResponse, ResponseData> saveOauthUser = userDto -> {
        if (userRepository.existsByEmail(userDto.getEmail())){
            UserDetails userDetails = userService.loadUserByUsername(userDto.getEmail());

            String token = utils.createJwt.apply(userDetails);

            UserResponse userResponse = userService.getUserResponse((User)userDetails);
            return new ResponseData(token, userResponse);
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

            UserResponse userResponse = getAuthResponse(userDto, savedUser);
            return new ResponseData(utils.createJwt.apply(userService.loadUserByUsername(user.getUsername())), userResponse);
        }
    };

    public UserResponse getAuthResponse(OAuthResponse authDto, User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirst_name(authDto.getFirst_name());
        userResponse.setLast_name(authDto.getLast_name());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getUserRole().name());
        userResponse.setImr_url(authDto.getImg_url());
        userResponse.setCreated_at(user.getCreatedAt());
        return userResponse;
    }

    private Profile populateProfile(OAuthResponse user){
        Profile profile = new Profile();
        profile.setFirstName(user.getFirst_name());
        profile.setLastName(user.getLast_name());
        profile.setAvatarUrl(user.getImg_url());

        return profileRepository.save(profile);
    }

    public ApiResponse facebookOauthUserJWT(OAuthDto payload) {
        OAuthResponse user =  getUserFromFacebookToken.apply(payload.getIdToken());
        ResponseData data = saveOauthUser.apply(user);
        return new ApiResponse(HttpStatus.OK.value(), "Login Successful!", data);
    }
}