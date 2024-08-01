package hng_java_boilerplate.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembershipInviteDto {
    private String email;
    private String organization;
    private String expires_at;
}
