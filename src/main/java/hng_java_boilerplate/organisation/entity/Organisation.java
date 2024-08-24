package hng_java_boilerplate.organisation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.util.UUIDGenarator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organisations")
@Data
public class Organisation {
    @Id
    private String id;

    private String name;
    private String description;

    private String slug;
    private String email;
    private String industry;
    private String type;
    private String country;
    private String address;
    private String state;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String owner;

    @ManyToMany(mappedBy = "organisations")
    @JsonIgnore
    private List<User> users;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
