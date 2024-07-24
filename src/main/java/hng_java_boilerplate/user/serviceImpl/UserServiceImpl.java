package hng_java_boilerplate.user.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.dto.request.LoginDto;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.dto.response.*;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.exception.EmailAlreadyExistsException;
import hng_java_boilerplate.user.exception.InvalidRequestException;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
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

    private UserResponse getUserResponse(User user){
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

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exist");
        }
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    @Override
    public GetUserDto getUserWithDetails(String userId) throws BadPaddingException {
        User user = userRepository.findById(userId)
                .orElseThrow(BadPaddingException::new);

        GetUserDto userDto = GetUserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        GetUserDto.ProfileDto profile = GetUserDto.ProfileDto
                .builder()
                .first_name(user.getProfile().getFirstName())
                .last_name(user.getProfile().getLastName())
                .phone(user.getProfile().getPhone())
                .avatar_url(user.getProfile().getAvatarUrl())
                .build();

        List<GetUserDto.OrganisationDto> organisations = user.getOrganisations()
                .stream()
                .map((org) -> GetUserDto.OrganisationDto
                        .builder()
                        .org_id(org.getId())
                        .name(org.getName())
                        .description(org.getDescription())
                        .build()).toList();

        List<GetUserDto.ProductDto> products = user.getProducts()
                .stream().map((product) -> GetUserDto.ProductDto
                        .builder()
                        .product_id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .build()).toList();

        userDto.setProfile(profile);
        userDto.setProducts(products);
        userDto.setOrganisations(organisations);

        return userDto;
    };

    @Override
    public ResponseEntity<?> loginUser(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginErrorResponse("Invalid request parameters", "Invalid email or password."));
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginErrorResponse("Authentication failed", "Invalid email or password."));
        }

        String token = jwtUtils.createJwt.apply(user);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
