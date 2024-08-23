package hng_java_boilerplate.authentication.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthLastUserResponse {
    private UserOAuthDetails user;
}
