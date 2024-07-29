package hng_java_boilerplate.profile.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.profile.entity.Profile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_that_updateUserProfile_updates_profile_with_status_200() throws Exception {
        String user_id = "";
        Profile profile = Profile.builder()
                .firstName("Udoh")
                .lastName("Udoh")
                .phone("+1234567890")
                .avatarUrl("https://example.com/avatar.jpg")
                .pronouns("They/Them")
                .jobTitle("Senior Developer")
                .department("Engineering")
                .social("@john_doe")
                .bio("Experienced developer with a passion for creating innovative solutions.")
                .recoveryEmail("john.doe.recovery@example.com")
                .build();

//        String progileJson = objectMapper.


        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/profile/{user_id}", user_id)
                        .contentType(MediaType.APPLICATION_JSON)
//                        .content()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }
}
