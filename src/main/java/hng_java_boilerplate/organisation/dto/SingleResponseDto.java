package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleResponseDto {
    private String message;
    private List<InvitationLink> Data;
    private Integer status;
}
