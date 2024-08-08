package hng_java_boilerplate.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthUserResponse {
    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private String fullname;
    private String role;
    private String access_token;
}
