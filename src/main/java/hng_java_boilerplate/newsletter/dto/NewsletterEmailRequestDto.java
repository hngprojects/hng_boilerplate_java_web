package hng_java_boilerplate.newsletter.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import hng_java_boilerplate.validEmail.ValidEmailCom;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsletterEmailRequestDto {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @ValidEmailCom(message = "Email Invalid: Does not contain '.com' or other valid parameters" )
    private String email;

}
