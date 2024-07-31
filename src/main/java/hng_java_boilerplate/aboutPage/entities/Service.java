package hng_java_boilerplate.aboutPage.entities;


import jakarta.persistence.Embeddable;
import lombok.Data;


@Data
@Embeddable
public class Service {

    private String titles;
    private String description;

    public Service(String titles, String description) {
        this.titles = titles;
        this.description = description;
    }

}
