package hng_java_boilerplate.profile.serviceImpl;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.profile.dto.response.*;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.profile.repository.ProfileRepository;
import hng_java_boilerplate.profile.service.ProfileService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    private static final String UPLOAD_DIR = "profile_photos";

    static {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }


    @Override
    public DeactivateUserResponse deactivateUser(DeactivateUserRequest request) {
        User authUser = userService.getLoggedInUser();

        String confirmation = request.getConfirmation().toLowerCase();

        if (confirmation.equals("true") && authUser.getIsDeactivated()) {
            throw new BadRequestException("User has been deactivated");
        }

        if (!confirmation.equals("true"))
            throw new BadRequestException("Confirmation needs to be true for deactivation");

        authUser.setIsDeactivated(true);
        userRepository.save(authUser);

        //todo: call email service to notify user of account deactivated

        return new DeactivateUserResponse(200, "Account Deactivated Successfully");
    }

    @Override
    public Optional<?> updateUserProfile(String id, UpdateUserProfileDto updateUserProfileDto) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Profile profile = user.get().getProfile();

            profile.setFirstName(updateUserProfileDto.getFirstName());
            profile.setLastName(updateUserProfileDto.getLastName());
            profile.setJobTitle(updateUserProfileDto.getJobTitle());
            profile.setPronouns(updateUserProfileDto.getPronouns());
            profile.setJobTitle(updateUserProfileDto.getJobTitle());
            profile.setDepartment(updateUserProfileDto.getDepartment());
            profile.setSocial(updateUserProfileDto.getSocial());
            profile.setBio(updateUserProfileDto.getBio());
            profile.setPhone(updateUserProfileDto.getPhoneNumber());
            profile.setAvatarUrl(updateUserProfileDto.getAvatarUrl());

            profile = profileRepository.save(profile);
            return Optional.of(ProfileUpdateResponseDto.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile updated successfully")
                    .data(profile)
                    .build()
            );
        }
        throw new NotFoundException("User not found");
    }

    @Override
    public ProfileResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found with id"));

        Profile profile = user.getProfile();
        ProfileDto profileDto = ProfileDto.builder()
                .id(profile.getId())
                .first_name(profile.getFirstName())
                .last_name(profile.getLastName())
                .job_title(profile.getJobTitle())
                .avatar_url(profile.getAvatarUrl())
                .bio(profile.getBio())
                .department(profile.getDepartment())
                .social(profile.getSocial())
                .phone_number(profile.getPhone())
                .pronouns(profile.getPronouns())
                .build();

        return new ProfileResponse(200, "user profile", profileDto);
    }

        @Override
        public ResponseEntity<ProfilePictureResponse> uploadProfileImage(MultipartFile file) {
            if (file.isEmpty() || !isValidImage(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ProfilePictureResponse(false, "Invalid file type or missing image. Only JPG or JPEG formats are allowed.", null));
            }
            try {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + File.separator + filename);
                Files.write(filePath, file.getBytes());

                String fileUrl = "https://hng.com/profile/uploads/" + filename;
                return ResponseEntity.ok(new ProfilePictureResponse(true, "Profile image uploaded successfully", fileUrl));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ProfilePictureResponse(false, "An unexpected error occurred while processing your request. Please try again later.", null));
            }
        }

    private boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/jpg"));
    }
}
