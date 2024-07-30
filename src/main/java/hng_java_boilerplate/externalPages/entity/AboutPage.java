package hng_java_boilerplate.externalPages.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "aboutPage")
public class AboutPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String introduction;
    @OneToOne(mappedBy = "aboutPage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private CustomSection custom_sections;
}
