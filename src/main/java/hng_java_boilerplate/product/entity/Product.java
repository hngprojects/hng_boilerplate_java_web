package hng_java_boilerplate.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Table(name = "products")
@Builder
public class Product {
    @Id
    private String id;

    private String name;
    private String description;
    private String category;
    private double price;
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "org_id", nullable = false)
    private Organisation organisation;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
