package hng_java_boilerplate.about_page.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "about_page")
public class AboutPage implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(insertable=false, updatable=false)
    private String title;

    @Column(name = "introduction", nullable = false)
    private String introduction;


    @Embedded
    private CustomSections customSections;






    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Embeddable
    public static class CustomSections {

        @Embedded
        private Stats stats;

        @Embedded
        private Services services;



    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Embeddable
    public static class Stats {

        private int yearsInBusiness;
        private int customers;
        private int monthlyBlogReaders;
        private int socialFollowers;



    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Embeddable
    public static class Services {

        //@Column(insertable=false, updatable=false)
        private String title;
        private String description;


    }
}
