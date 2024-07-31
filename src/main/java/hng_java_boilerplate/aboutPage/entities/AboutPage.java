package hng_java_boilerplate.aboutPage.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class AboutPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String introduction;

    @Embedded
    private CustomSections customSections;

    private LocalDateTime lastUpdated;

    public AboutPage() {
        this.title = "More Than Just A BoilerPlate";
        this.introduction = "Welcome to Hng Boilerplate, where passion meets innovation.";
        this.customSections = new CustomSections();
        this.lastUpdated = LocalDateTime.now();
    }
}
