package hng_java_boilerplate.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Table(name = "products")
public class Product {
    @Id
    private String id;

    private String name;
    private String description;
    private String category;
    private double price;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "current_stock")
    private double currentStock;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated; // This will be for stock

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
