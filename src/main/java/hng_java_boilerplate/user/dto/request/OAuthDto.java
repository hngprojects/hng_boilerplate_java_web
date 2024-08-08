package hng_java_boilerplate.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthDto {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("expires_at")
    private long expiresAt;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("type")
    private String type;

    @JsonProperty("provider_account_id")
    private String providerAccountId;

    @JsonProperty("verified_user_id")
    private String verifiedUserId;
}
