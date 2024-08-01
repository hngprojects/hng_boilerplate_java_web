package hng_java_boilerplate.waitlist.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Waitlist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "fullName is required")
    private String fullName;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    @Column(name = "signup_date")
    private LocalDateTime signupDate;

    @PrePersist
    protected void onCreate() {
        signupDate = LocalDateTime.now();
    }
}
