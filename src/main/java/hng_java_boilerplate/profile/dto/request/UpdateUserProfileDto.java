package hng_java_boilerplate.profile.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserProfileDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("pronouns")
    private String pronouns;

    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("department")
    private String department;

    @JsonProperty("social")
    private String social;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("avatar_url")
    private String avatarUrl;
}
