package hng_java_boilerplate.helpCenter.faq.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "faqs")
public class FAQ {
    @Id
    private String id;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String answer;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
