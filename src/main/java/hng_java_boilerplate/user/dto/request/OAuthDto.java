package hng_java_boilerplate.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthDto {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String img_url;
    private boolean is_active;
}
