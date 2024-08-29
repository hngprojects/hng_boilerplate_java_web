package hng_java_boilerplate.user.dto.response;

import hng_java_boilerplate.organisation.entity.Organisation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String role;
    private String imr_url;
    private List<Organisation> organisations;
    private LocalDateTime created_at;
}
