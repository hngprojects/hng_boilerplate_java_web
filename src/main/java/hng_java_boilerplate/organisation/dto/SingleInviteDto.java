package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleInviteDto {
    private String Organisation_id;
    @Email
    private String email;
}
