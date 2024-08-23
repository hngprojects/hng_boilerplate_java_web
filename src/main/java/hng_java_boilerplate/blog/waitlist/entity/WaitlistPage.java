package hng_java_boilerplate.blog.waitlist.entity;

import jakarta.persistence.*;
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
public class WaitlistPage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String pageTitle;

    @Column(nullable = false, unique = true)
    private String urlSlug;

    @Column(nullable = false)
    private String headlineText;

    @Column(nullable = false)
    private String subheadlineText;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bodyText;

    private boolean active;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}