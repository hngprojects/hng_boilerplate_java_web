package hng_java_boilerplate.about_page.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AboutPageDto {

    private Long id;
    private String title;
    private String introduction;

    @Embedded
    private AboutPageDto.CustomSections customSections;

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Embeddable
    public static class CustomSections {
        @Embedded
        private AboutPageDto.Stats stats;
        @Embedded
        private AboutPageDto.Services services;
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
        private String title;
        private String description;
    }
}


