package hng_java_boilerplate.user.serviceImpl;

import hng_java_boilerplate.activitylog.service.ActivityLogService;
import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.dto.request.EmailSenderDto;
import hng_java_boilerplate.plans.entity.Plan;
import hng_java_boilerplate.plans.service.PlanService;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.dto.request.LoginDto;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.dto.request.*;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.MembersResponse;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.dto.response.UserResponse;
import hng_java_boilerplate.user.entity.MagicLinkToken;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.dto.response.UserResponse;
import hng_java_boilerplate.user.dto.response.*;
import hng_java_boilerplate.user.entity.PasswordResetToken;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.entity.VerificationToken;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.repository.MagicLinkTokenRepository;
import hng_java_boilerplate.user.repository.PasswordResetTokenRepository;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.repository.VerificationTokenRepository;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static hng_java_boilerplate.util.PaginationUtils.getPaginatedUsers;
import static hng_java_boilerplate.util.PaginationUtils.validatePageNumber;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final ActivityLogService activityLogService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailServiceImpl emailService;
    private final OrganisationRepository organisationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PlanService planService;
    private final MagicLinkTokenRepository magicLinkTokenRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws BadRequestException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new BadRequestException("invalid credential");
        }
        if (user.get().getIsDeactivated()) {
            throw new DisabledException("User is deactivated");
        }
        return user.get();
    }

    @Override
    public ResponseEntity<ApiResponse<ResponseData>> registerUser(SignupDto signupDto) {
        validateEmail(signupDto.getEmail());

        User user = new User();
        Plan plan = planService.findOne("1");
        user.setPlan(plan);
        user.setName(signupDto.getFirstName().trim() + " " + signupDto.getLastName().trim());
        user.setUserRole(Role.ROLE_USER);
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        User savedUser = userRepository.save(user);
        createDefaultOrganisation(savedUser);

        Optional<User> createdUserCheck = userRepository.findByEmail(user.getEmail());
        if (createdUserCheck.isEmpty()) {
            throw new BadRequestException("Registration Unsuccessful");
        }

        String token = jwtUtils.createJwt.apply(loadUserByUsername(savedUser.getEmail()));

        UserResponse userResponse = getUserResponse(savedUser);
        ResponseData data = new ResponseData(userResponse);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "Registration Successful!", token, data), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse<ResponseData>> loginUser(LoginDto loginDto) {
        UserDetails userDetails = loadUserByUsername(loginDto.getEmail());
        User user = (User) userDetails;
        boolean isValidPassword =
                passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword());
        if (!isValidPassword) {
            throw new BadRequestException("Invalid email or password");
        }
        String token = jwtUtils.createJwt.apply(userDetails);
        UserResponse userResponse = getUserResponse(user);
        ResponseData data = new ResponseData(userResponse);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Login Successful!", token, data), HttpStatus.OK);
    }

    @Override
    public void sendMagicLink(String email, HttpServletRequest request) {
        String token = UUID.randomUUID().toString();
        if (userRepository.existsByEmail(email)){
            magicLinkTokenRepository.save(new MagicLinkToken(userRepository.findByEmail(email).get(), token));
            emailService.sendMagicLink(email, request, token);
        }
        else {
            User user = saveMagicLinkUser(email);
            magicLinkTokenRepository.save(new MagicLinkToken(user, token));
            emailService.sendMagicLink(email, request, token);
        }
    }

    private User saveMagicLinkUser(String email){
        String password = UUID.randomUUID().toString();
        User user = new User();
        user.setName("No name");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    @Override
    public void requestToken(EmailSenderDto emailSenderDto, HttpServletRequest request) {
        User user = findUserByEmail(emailSenderDto.getEmail());
        String token = emailService.generateToken();
        saveVerificationTokenForUser(user, token);
        emailService.sendVerificationEmail(user, request, token);
    }

    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public void requestToken(EmailSenderDto emailSenderDto, HttpServletRequest request) {
        User user = findUserByEmail(emailSenderDto.getEmail());
        String token = emailService.generateToken();
        saveVerificationTokenForUser(user, token);
        emailService.sendVerificationEmail(user, request, token);
    }

    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public ResponseEntity<String> verifyOtp(String email, String token, HttpServletRequest request) {
        String tokenValidationResult = validateVerificationToken(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BadRequestException("User not found with email: " + email);
        }
        User user = userOptional.get();
        if (tokenValidationResult.equals("invalid")) {
            throw new UnAuthorizedException("Invalid OTP");
        }
        if (tokenValidationResult.equals("expired")) {
            String newToken = emailService.generateToken();
            emailService.sendVerificationEmail(user, request, newToken);
            throw new BadRequestException("OTP has expired. A new OTP has been sent to your email.");
        }
        user.setIsEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok("User verified successfully");
    }

    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "invalid";
        }
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime() - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public void forgotPassword(EmailSenderDto emailSenderDto, HttpServletRequest request) {
        User user = findUserByEmail(emailSenderDto.getEmail());
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        emailService.passwordResetTokenMail(user, request, token);
    }

    private void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken newlyCreatedPasswordResetToken = new PasswordResetToken(user, token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserId(user.getId());
        if(passwordResetToken != null){
            passwordResetTokenRepository.delete(passwordResetToken);
        }
        passwordResetTokenRepository.save(newlyCreatedPasswordResetToken);
    }

    @Override
    public ResponseEntity<String> resetPassword(String token, ResetPasswordDto passwordDto) {
        String result = validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            throw new UnAuthorizedException("Invalid Token");
        }
        Optional<User> user = Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
        if (user.isPresent()) {
            changePassword(user.get(), passwordDto.getNew_password());
            return new ResponseEntity<>("Password Reset Successful", HttpStatus.OK);
        } else {
            throw new UnAuthorizedException("Invalid Token");
        }
    }

    private String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            return "invalid";
        }
        Calendar cal = Calendar.getInstance();
        if (passwordResetToken.getExpirationTime().getTime()
                - cal.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        return "valid";
    }

    private void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User findUserByEmail(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("User with email " + username + " not found"));
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

    @Override
    public User findUser(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Map<String, String> splitName(User user){
        String[] nameParts = user.getName().split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        Map<String, String> nameDict = new HashMap<>();
        nameDict.put("firstName", firstName);
        nameDict.put("lastName", lastName);
        return nameDict;
    }

    // GetUserResponse method that combines both branches
    public UserResponse getUserResponse(User user) {
        Map<String, String> splitName = splitName(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirst_name(splitName.get("firstName"));
        userResponse.setLast_name(splitName.get("lastName"));
        userResponse.setEmail(user.getEmail());
        userResponse.setOrganisations(user.getOrganisations());
        userResponse.setCreated_at(user.getCreatedAt());
        return userResponse;
    }

    private void createDefaultOrganisation(User user){
        Organisation organisation = new Organisation();
        organisation.setName(splitName(user).get("firstName") + "'s Organisation");
        organisation.setOwner(user.getId());
        organisation.setDescription("Default organisation for " + splitName(user).get("firstName"));
        organisation.setUsers(Collections.singletonList(user));
        user.setOrganisations(Collections.singletonList(organisation));
        organisationRepository.save(organisation);
    }

    // Validate email method to check if the email already exists
    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists");
        }
    }

    // Get the currently logged-in user
    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
    }

    // Get user with details
    @Override
    @Transactional
    public GetUserDto getUserWithDetails(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id"));

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

    @Override
    public List<MembersResponse> getAllUsers(int page, Authentication authentication) {
        List<MembersResponse> users = new ArrayList<>();
        User user  = (User) authentication.getPrincipal();
        if (user != null) {
            List<User> allUser = userRepository.findAll();
            validatePageNumber(page, allUser);
            Page<User> paginatedMembers = getPaginatedUsers(page, allUser);
            users = paginatedMembers.stream().map(member -> MembersResponse.builder()
                    .fullName(member.getName()).email(member.getEmail()).createdAt(member.getCreatedAt().toString())
                   .build()).collect(Collectors.toList());
        }
        return users;
    }

    @Override
    public Response<?> getUserById(String userId, Authentication authentication) {
        String email = authentication.getName();

        if (!userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email does not exist");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User foundUser = userOptional.get();
            Map<String, String> data = new LinkedHashMap<>();
            data.put("id", foundUser.getId());
            data.put("fullname", foundUser.getName());
            data.put("email", foundUser.getEmail());
            data.put("role", foundUser.getUserRole().toString());
            data.put("createdAt", foundUser.getCreatedAt() != null ? foundUser.getCreatedAt().toString() : "N/A");
            return Response.builder().status_code("200").message("User data successfully fetched").data(data).build();
        } else {
            throw new NotFoundException("User not found with id: " + userId);
        }
    }

}
