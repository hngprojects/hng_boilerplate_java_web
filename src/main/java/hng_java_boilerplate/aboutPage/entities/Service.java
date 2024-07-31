package hng_java_boilerplate.aboutPage.entities;


import jakarta.persistence.Embeddable;
import lombok.Data;


@Data
@Embeddable
public class Service {

    private String title;
    private String description;

    public Service(String title, String description) {
        this.title = title;
        this.description = description;
    }

}
