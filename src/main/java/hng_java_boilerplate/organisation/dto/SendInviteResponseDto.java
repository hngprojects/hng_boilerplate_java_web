package hng_java_boilerplate.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendInviteResponseDto {

    private String messages;
    private List<MembershipInviteDto>invitations;
}
