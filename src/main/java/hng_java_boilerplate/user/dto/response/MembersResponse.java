package hng_java_boilerplate.user.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class MembersResponse {

    private String fullName;
    private String email;
    private String phone;
    private String status;
    private String createdAt;


}
