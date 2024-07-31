package hng_java_boilerplate.aboutPage.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Stats {
    private int yearsInBusiness;
    private int customers;
    private int monthlyBlogReaders;
    private int socialFollowers;

    public Stats(int yearsInBusiness, int customers, int monthlyBlogReaders, int socialFollowers) {
        this.yearsInBusiness = yearsInBusiness;
        this.customers = customers;
        this.monthlyBlogReaders = monthlyBlogReaders;
        this.socialFollowers = socialFollowers;
    }
}
