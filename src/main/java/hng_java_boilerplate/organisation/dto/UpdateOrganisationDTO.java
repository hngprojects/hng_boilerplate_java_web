package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrganisationDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    @Email(message = "Email should be valid")
    private String email;

    private String industry;

    private String type;

    private String country;

    private String address;

    private String state;

    @Override
    public String toString() {
        return "UpdateOrganisationDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", industry='" + industry + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}