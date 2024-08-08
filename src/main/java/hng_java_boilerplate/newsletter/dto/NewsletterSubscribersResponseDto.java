package hng_java_boilerplate.newsletter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hng_java_boilerplate.newsletter.entity.NewsletterSubscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsletterSubscribersResponseDto {

    private String message;
    private boolean success;
    private int status_code;
    private List<NewsletterSubscription> newsletterSubscribersData;

    public NewsletterSubscribersResponseDto(List<NewsletterSubscription> newsletterSubscribersData) {
        this.newsletterSubscribersData = newsletterSubscribersData;
    }
}
