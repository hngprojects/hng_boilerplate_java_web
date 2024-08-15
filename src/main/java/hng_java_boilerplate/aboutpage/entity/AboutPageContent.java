package hng_java_boilerplate.aboutpage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AboutPageContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String introduction;

    private Integer yearsInBusiness;
    private Integer customers;
    private Integer monthlyBlogReaders;
    private Integer socialFollowers;

    private String servicesTitle;
    private String servicesDescription;
}
