package hng_java_boilerplate.organisation.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString
@Setter
@Getter
public class MembersResponse {

    private String fullName;
    private String email;
    private String phone;
    private String createdAt;
    private String status;

}
