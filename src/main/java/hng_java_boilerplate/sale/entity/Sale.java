package hng_java_boilerplate.sale.entity;

import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> saleItems;

    private double totalSale;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Transient
    public String getMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return this.createdAt.format(formatter);
    }
}

