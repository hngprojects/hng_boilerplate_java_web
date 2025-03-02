package hng_java_boilerplate.profile.serviceImpl;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.profile.dto.response.ProfilePictureResponse;
import hng_java_boilerplate.profile.dto.response.ProfileUpdateResponseDto;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.profile.repository.ProfileRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.utils.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfileUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl underTest;

    @Test
    public void test_that_updateUserProfile_updates_and_returns_status_200(){

        UpdateUserProfileDto profile = TestDataUtil.createUserProfileDto();
        Profile profileEntity = TestDataUtil.createUserProfileEntity();

        User user = new User();
        user.setName("unyime unyime");
        user.setUserRole(Role.ROLE_USER);
        user.setEmail("unyime1@gmail.com");
        user.setPassword("123456");
        user.setProfile(profileEntity);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(profileRepository.save(profileEntity)).thenReturn(profileEntity);

        Optional<?> updatedProfile = underTest.updateUserProfile(user.getId(), profile);
        assertThat(updatedProfile).isPresent();
        assertThat(updatedProfile.get()).isEqualTo(
                ProfileUpdateResponseDto.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Profile updated successfully")
                        .data(profileEntity)
                        .build()
        );
    }

    @Test
    public void test_that_updateUserProfile_returns_error_with_status_400_when_user_not_found(){

        UpdateUserProfileDto profile = TestDataUtil.createUserProfileDto();
        Profile profileEntity = TestDataUtil.createUserProfileEntity();

        User user = new User();
        user.setName("unyime unyime");
        user.setUserRole(Role.ROLE_USER);
        user.setEmail("unyime1@gmail.com");
        user.setPassword("123456");
        user.setProfile(profileEntity);

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.updateUserProfile(user.getId(), profile))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void test_uploadProfileImage_returns_successful_response() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "dummy image content".getBytes()
        );

        ResponseEntity<ProfilePictureResponse> response = underTest.uploadProfileImage(file);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("Profile image uploaded successfully");
        assertThat(response.getBody().getImageUrl()).isNotBlank();
    }

    @Test
    public void test_uploadProfileImage_returns_error_for_invalid_file() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "invalid file content".getBytes()
        );

        ResponseEntity<ProfilePictureResponse> response = underTest.uploadProfileImage(file);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid file type or missing image. Only JPG or JPEG formats are allowed.");
    }

}
