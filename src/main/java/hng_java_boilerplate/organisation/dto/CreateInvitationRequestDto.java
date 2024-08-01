package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvitationRequestDto {
    @Email
    private List<String> email;
    private String organisation_id;
}
