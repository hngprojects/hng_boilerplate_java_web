package hng_java_boilerplate.squeeze.entity;

import hng_java_boilerplate.squeeze.util.StringListConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "squeeze_request")
public class SqueezeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String first_name;

    @NotBlank
    private String last_name;

    @NotBlank
    private String phone;

    @NotBlank
    private String location;

    @NotBlank
    private String job_title;

    @NotBlank
    private String company;

    @NotEmpty
    @Convert(converter = StringListConverter.class)
    private List<String> interests = new ArrayList<>();

    @NotBlank
    private String referral_source;

}
