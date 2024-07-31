package hng_java_boilerplate.aboutPage.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "custom_section")
public class CustomSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "custom_sections", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private StatEntity stats;
    @OneToOne(mappedBy = "custom_sections", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private ServiceEntity services;
    @OneToOne
    @JoinColumn(name = "aboutPage_id")
    @JsonBackReference
    private AboutPage aboutPage;

}
