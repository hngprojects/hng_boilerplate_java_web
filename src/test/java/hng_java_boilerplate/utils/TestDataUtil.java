package hng_java_boilerplate.utils;

import hng_java_boilerplate.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;

public final class TestDataUtil {

    public static User createRegionEntity(){
         User user = new User();
        user.setName("unyime unyime");
        user.setUserRole(Role.ROLE_USER);
        user.setEmail("unyime1@gmail.com");
        user.setPassword("123456");
        user.setProfile(createUserProfileEntity());
        return user;
    }

    public static UpdateUserProfileDto createUserProfileDto(){
        return UpdateUserProfileDto.builder()
                .firstName("Udoh")
                .lastName("Udoh")
                .phoneNumber("+1234567890")
                .avatarUrl("https://example.com/avatar.jpg")
                .pronouns("They/Them")
                .jobTitle("Senior Developer")
                .department("Engineering")
                .social("@john_doe")
                .bio("Experienced developer with a passion for creating innovative solutions.")
                .build();
    }

    public static Profile createUserProfileEntity(){
        return Profile.builder()
                .id("001")
                .firstName("Udoh")
                .lastName("Udoh")
                .phone("+1234567890")
                .avatarUrl("https://example.com/avatar.jpg")
                .pronouns("They/Them")
                .jobTitle("Senior Developer")
                .department("Engineering")
                .social("@john_doe")
                .bio("Experienced developer with a passion for creating innovative solutions.")
                .build();
    }
}
