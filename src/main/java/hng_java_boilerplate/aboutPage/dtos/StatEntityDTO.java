package hng_java_boilerplate.aboutPage.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StatEntityDTO {
    private int years_in_business;
    private int customers;
    private int monthly_blog_readers;
    private int social_followers;
}
