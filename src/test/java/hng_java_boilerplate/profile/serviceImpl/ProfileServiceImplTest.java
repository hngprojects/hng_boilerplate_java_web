package hng_java_boilerplate.profile.serviceImpl;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.profile.repository.ProfileRepository;
import hng_java_boilerplate.profile.service.ProfileService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ProfileServiceImpl underTest;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private MultipartFile file;

    @InjectMocks
    private ProfileService profileService;

    private Profile profile;
    private User user;
    private final String storagePath = "/images/";

    @BeforeEach
    public void setUp() {
        user = new User(); // Initialize user
        profile = new Profile();
        profile.setUser(user);
    }

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
    public void testUploadProfileImage() throws IOException {
        String originalFilename = "test.jpg";
        String expectedFilename = UUID.randomUUID().toString() + "-" + originalFilename;

        when(file.getOriginalFilename()).thenReturn(originalFilename);
        when(profileRepository.findByUserId(user.getId())).thenReturn(Optional.of(profile));

        String result = profileService.uploadProfileImage(file, user.getId());

        assertEquals(expectedFilename, result);
        verify(profileRepository).save(profile);
        assertEquals(storagePath + expectedFilename, profile.getAvatarUrl());

        verify(file, times(1)).getOriginalFilename();
        verify(profileRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    public void testUploadProfileImage_ProfileNotFound() {
        when(profileRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            profileService.uploadProfileImage(file, user.getId());
        });

        verify(profileRepository, never()).save(any(Profile.class));
    }
}
