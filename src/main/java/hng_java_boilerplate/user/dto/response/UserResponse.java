package hng_java_boilerplate.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String avatar_url;
    private LocalDateTime created_at;
}
