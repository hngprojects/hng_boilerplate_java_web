package hng_java_boilerplate.testimonials.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "testimonials")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String user_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private LocalDate created_at;

    @Column(nullable = false)
    private LocalDate updated_at;
}