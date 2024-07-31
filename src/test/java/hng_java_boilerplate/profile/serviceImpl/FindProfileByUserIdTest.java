package hng_java_boilerplate.profile.serviceImpl;

import hng_java_boilerplate.profile.dto.response.ProfileData;
import hng_java_boilerplate.profile.dto.response.ProfileResponse;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FindProfileByUserIdTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFidByUserId_UserFound() {
        User user = new User();
        Profile profile = new Profile();
        profile.setId("profile-id");
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setPhone("1234567890");
        profile.setAvatarUrl("http://example.com/avatar.jpg");

        user.setProfile(profile);
        when(userRepository.findById("user-id")).thenReturn(Optional.of(user));

        ProfileResponse response = userService.findByUserId("user-id");

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Profile found", response.getMessage());
        ProfileData profileData = response.getData();
        assertNotNull(profileData);
        assertEquals("profile-id", profileData.getId());
        assertEquals("John", profileData.getFirstName());
        assertEquals("Doe", profileData.getLastName());
        assertEquals("1234567890", profileData.getPhone());
        assertEquals("http://example.com/avatar.jpg", profileData.getAvatarUrl());
    }

    @Test
    void testFidByUserId_UserNotFound() {
        when(userRepository.findById("user-id")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.findByUserId("user-id");
        });

        assertEquals("User not found", exception.getMessage());
    }
}
