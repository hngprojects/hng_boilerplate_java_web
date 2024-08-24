package hng_java_boilerplate.profile.serviceImpl;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.profile.dto.response.ProfileResponse;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ProfileServiceImpl underTest;

    @Test
    void shouldDeactivateUser() {
        // mock auth user;
        User authUser = new User();
        authUser.setName("Test User");
        authUser.setEmail("test@user.com");
        authUser.setIsDeactivated(false);

        // set request
        DeactivateUserRequest request = DeactivateUserRequest
                .builder()
                .reason("private")
                .confirmation("true")
                .build();

        // mock response
        DeactivateUserResponse response =
                new DeactivateUserResponse(200, "Account Deactivated Successfully");

        when(userService.getLoggedInUser()).thenReturn(authUser);
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setIsDeactivated(true);
            return null;
        }).when(userRepository).save(any(User.class));

        DeactivateUserResponse res = underTest.deactivateUser(request);

        verify(userRepository, times(1)).save(authUser);

        assertThat(res.status_code()).isEqualTo(response.status_code());
        assertThat(res.message()).isEqualTo(response.message());
    }

    @Test
    void shouldThrowWhenConfirmationNotTrue() {
        // mock auth user;
        User authUser = new User();
        authUser.setName("Test User");
        authUser.setEmail("test@user.com");

        // set request
        DeactivateUserRequest request = DeactivateUserRequest
                .builder()
                .confirmation("false")
                .build();

        when(userService.getLoggedInUser()).thenReturn(authUser);

        assertThatThrownBy(() -> underTest.deactivateUser(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Confirmation needs to be true for deactivation");
    }

    @Test
    void shouldThrowWhenNoAuthUser() {
        // mock request
        DeactivateUserRequest request = DeactivateUserRequest.builder().build();
        assertThatThrownBy(() -> underTest.deactivateUser(request));
    }

    @Test
    void shouldThrowWhenUserAlreadyDeactivated() {
        // mock auth user
        User authUser = new User();
        authUser.setName("Test User");
        authUser.setEmail("test@user.com");
        authUser.setIsDeactivated(true);

        // set request
        DeactivateUserRequest request = DeactivateUserRequest
                .builder()
                .confirmation("true")
                .build();

        when(userService.getLoggedInUser()).thenReturn(authUser);

        assertThatThrownBy(() -> underTest.deactivateUser(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("User has been deactivated");
    }

    @Test
    void shouldGetUserProfile() {
        Profile profile = new Profile();
        profile.setId("profile-id");
        profile.setFirstName("first_name");
        profile.setLastName("last_name");

        User user = new User();
        user.setId("user-id");
        user.setProfile(profile);

        when(userRepository.findById("user-id")).thenReturn(Optional.of(user));

        ProfileResponse response = underTest.getUserProfile("user-id");

        assertThat(response.status_code()).isEqualTo(200);
        assertThat(response.message()).isEqualTo("user profile");
        assertThat(response.data()).hasFieldOrPropertyWithValue("id", "profile-id");
        assertThat(response.data()).hasFieldOrPropertyWithValue("first_name", "first_name");
        assertThat(response.data()).hasFieldOrPropertyWithValue("last_name", "last_name");

        verify(userRepository).findById(anyString());
    }
}