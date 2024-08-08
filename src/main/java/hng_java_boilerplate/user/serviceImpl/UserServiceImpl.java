package hng_java_boilerplate.user.serviceImpl;

import hng_java_boilerplate.activitylog.service.ActivityLogService;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.dto.request.LoginDto;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.dto.response.UserResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.exception.EmailAlreadyExistsException;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.exception.UsernameNotFoundException;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final ActivityLogService activityLogService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        if (user.get().getIsDeactivated()) {
            throw new DisabledException("User is deactivated");
        }
        return user.get();
    }

    @Override
    public ResponseEntity<ApiResponse> registerUser(SignupDto signupDto) {
        validateEmail(signupDto.getEmail());

        User user = new User();
        user.setName(signupDto.getFirstName().trim() + " " + signupDto.getLastName().trim());
        user.setUserRole(Role.ROLE_USER);
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        User savedUser = userRepository.save(user);

        Optional<User> createdUserCheck = userRepository.findByEmail(user.getEmail());
        if (createdUserCheck.isEmpty()) {
            throw new UserNotFoundException("Registration Unsuccessful");
        }

        String token = jwtUtils.createJwt.apply(loadUserByUsername(savedUser.getEmail()));

        UserResponse userResponse = getUserResponse(savedUser);
        ResponseData data = new ResponseData(token, userResponse);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), "Registration Successful!", data), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> loginUser(LoginDto loginDto) {
        UserDetails userDetails = loadUserByUsername(loginDto.getEmail());
        User user = (User) userDetails;

        boolean isValidPassword = passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword());

        if (!isValidPassword) {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Invalid email or password", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        String token = jwtUtils.createJwt.apply(userDetails);

        UserResponse userResponse = getUserResponse(user);
        ResponseData data = new ResponseData(token, userResponse);

        // Log activity
        GetUserDto userDto = convertUserToGetUserDto(user);
        String organisationId = userDto.getOrganisations().isEmpty() ? null : userDto.getOrganisations().get(0).getOrg_id();
        activityLogService.logActivity(organisationId, user.getId(), "User logged in");
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), "Login Successful!", data), HttpStatus.OK);
    }

    // Convert User to GetUserDto
    private GetUserDto convertUserToGetUserDto(User user) {
        return GetUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .organisations(Optional.ofNullable(user.getOrganisations())
                        .orElseGet(List::of)
                        .stream()
                        .map(o -> GetUserDto.OrganisationDto.builder()
                                .org_id(o.getId())
                                .name(o.getName())
                                .description(o.getDescription())
                                .build())
                        .toList())
                .build();
    }

    // Save method for User entity
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    // GetUserResponse method that combines both branches
    public UserResponse getUserResponse(User user) {
        String[] nameParts = user.getName().split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirst_name(firstName);
        userResponse.setLast_name(lastName);
        userResponse.setEmail(user.getEmail());
        userResponse.setCreated_at(user.getCreatedAt());
        return userResponse;
    }

    // Validate email method to check if the email already exists
    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    // Get the currently logged-in user
    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Get user with details
    @Override
    @Transactional
    public GetUserDto getUserWithDetails(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id"));

        GetUserDto userDto = GetUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        GetUserDto.ProfileDto profile = GetUserDto.ProfileDto.builder()
                .first_name(user.getProfile().getFirstName())
                .last_name(user.getProfile().getLastName())
                .phone(user.getProfile().getPhone())
                .avatar_url(user.getProfile().getAvatarUrl())
                .build();

        userDto.setProfile(profile);
        userDto.setProducts(user.getProducts().stream().map(product -> GetUserDto.ProductDto.builder()
                .product_id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .build()).toList());

        userDto.setOrganisations(user.getOrganisations().stream().map(org -> GetUserDto.OrganisationDto.builder()
                .org_id(org.getId())
                .name(org.getName())
                .description(org.getDescription())
                .build()).toList());

        return userDto;
    }
}
