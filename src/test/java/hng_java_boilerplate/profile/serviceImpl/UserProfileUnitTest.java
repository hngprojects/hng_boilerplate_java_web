package hng_java_boilerplate.profile.serviceImpl;

import hng_java_boilerplate.authentication.profile.serviceImpl.ProfileServiceImpl;
import hng_java_boilerplate.exception.exception_class.NotFoundException;
import hng_java_boilerplate.authentication.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.authentication.profile.dto.response.ProfileUpdateResponseDto;
import hng_java_boilerplate.authentication.profile.entity.Profile;
import hng_java_boilerplate.authentication.profile.repository.ProfileRepository;
import hng_java_boilerplate.authentication.user.entity.User;
import hng_java_boilerplate.authentication.user.enums.Role;
import hng_java_boilerplate.authentication.user.repository.UserRepository;
import hng_java_boilerplate.utils.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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

}
