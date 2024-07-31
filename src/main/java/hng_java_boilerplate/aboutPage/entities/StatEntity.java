package hng_java_boilerplate.aboutPage.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "stat")
public class StatEntity {
    @Id
    @GeneratedValue
    private Long id;
    private int years_in_business;
    private int customers;
    private int monthly_blog_readers;
    private int social_followers;
    @OneToOne
    @JoinColumn(name = "custom_section_id")
    @JsonBackReference
    private CustomSection custom_sections;
}
