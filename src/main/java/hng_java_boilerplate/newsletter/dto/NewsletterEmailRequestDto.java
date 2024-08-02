package hng_java_boilerplate.newsletter.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String email;

}
