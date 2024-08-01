package hng_java_boilerplate.organisation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invitation_table")
public class Invitation {
    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(name = "user_email", nullable = false)
    private String userEmail;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "expires_at",nullable = false)
    private Timestamp expiresAt;
    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;


    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
