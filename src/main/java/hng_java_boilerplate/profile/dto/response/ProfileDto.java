package hng_java_boilerplate.profile.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDto {
    private String id;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String avatar_url;
    private String pronouns;
    private String job_title;
    private String department;
    private String social;
    private String bio;
}
