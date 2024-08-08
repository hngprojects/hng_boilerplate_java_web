package hng_java_boilerplate.newsletter.service;

import hng_java_boilerplate.newsletter.dto.NewsletterEmailRequestDto;
import hng_java_boilerplate.newsletter.dto.NewsletterEmailResponseDto;
import hng_java_boilerplate.newsletter.dto.NewsletterSubscribersResponseDto;
import hng_java_boilerplate.newsletter.entity.NewsletterSubscription;
import hng_java_boilerplate.newsletter.exception.EmailSubscriptionAlreadyExistException;
import hng_java_boilerplate.newsletter.repository.NewsletterSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsletterService {

    private final NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    @Autowired
    public NewsletterService(NewsletterSubscriptionRepository newsletterSubscriptionRepository) {
        this.newsletterSubscriptionRepository = newsletterSubscriptionRepository;
    }


    public NewsletterEmailResponseDto joinNewsletter(NewsletterEmailRequestDto subscriptionEmail)
            throws EmailSubscriptionAlreadyExistException {

        NewsletterEmailResponseDto newsletterEmailResponseDto = new NewsletterEmailResponseDto();


            Optional<NewsletterSubscription> emailExists = newsletterSubscriptionRepository.findByEmail(subscriptionEmail.getEmail());

            if ((emailExists.isEmpty())) {

                NewsletterSubscription newsletterSubscription = new NewsletterSubscription();
                newsletterSubscription.setEmail(subscriptionEmail.getEmail());
                newsletterSubscriptionRepository.save(newsletterSubscription);

                newsletterEmailResponseDto.setMessage("Your Subscription was Successful");
                newsletterEmailResponseDto.setSuccess(true);
                newsletterEmailResponseDto.setStatus_code(201);

                return newsletterEmailResponseDto;

            }

            else {
                throw new EmailSubscriptionAlreadyExistException("A subscription with " + subscriptionEmail.getEmail() + " already exists.");
            }



    }

    public NewsletterSubscribersResponseDto getListOfSubscribers(){

        NewsletterSubscribersResponseDto newsletterSubscribersResponseDto = new NewsletterSubscribersResponseDto();

        List<NewsletterSubscription> listOfSubscribers = newsletterSubscriptionRepository.findAll();

        newsletterSubscribersResponseDto.setMessage("List of Subscribers");
        newsletterSubscribersResponseDto.setSuccess(true);
        newsletterSubscribersResponseDto.setStatus_code(201);
        newsletterSubscribersResponseDto.setNewsletterSubscribersData(listOfSubscribers);

        return newsletterSubscribersResponseDto;


    }



}

