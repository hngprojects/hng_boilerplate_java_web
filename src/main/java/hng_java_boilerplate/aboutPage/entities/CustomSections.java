package hng_java_boilerplate.aboutPage.entities;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Embeddable
public class CustomSections {
    @Embedded
    private Stats stats;

    @Embedded
    private Service service;

    public CustomSections() {
        this.stats = new Stats(10, 75000, 100000, 1200000);
        this.service = new Service("Trained to Give You The Best", "Since our founding, Hng Boilerplate has been dedicated to constantly evolving to stay ahead of the curve.");
    }

}
