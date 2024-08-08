package hng_java_boilerplate.newsletter.e2e_test;

import hng_java_boilerplate.newsletter.dto.NewsletterEmailRequestDto;
import hng_java_boilerplate.newsletter.dto.NewsletterEmailResponseDto;
import hng_java_boilerplate.newsletter.dto.NewsletterSubscribersResponseDto;
import hng_java_boilerplate.newsletter.entity.NewsletterSubscription;
import hng_java_boilerplate.newsletter.exception.EmailSubscriptionAlreadyExistException;
import hng_java_boilerplate.newsletter.repository.NewsletterSubscriptionRepository;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsletterServiceTest {

    @Mock
    private NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    @InjectMocks
    private NewsletterService newsletterService;

    @Test
    void testJoinNewsletterSuccessful() {

        NewsletterEmailRequestDto subscriptionEmail = new NewsletterEmailRequestDto("esta@gmail.com");
        when(newsletterSubscriptionRepository.findByEmail(any())).thenReturn(Optional.empty());

        NewsletterEmailResponseDto response = newsletterService.joinNewsletter(subscriptionEmail);


        verify(newsletterSubscriptionRepository, times(1)).save(any());
        assertEquals("Your Subscription was Successful", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(201, response.getStatus_code());
    }

    @Test
    void testJoinNewsletterAlreadySubscribed() {

        NewsletterEmailRequestDto subscriptionEmail = new NewsletterEmailRequestDto("olu@gmail.com");
        when(newsletterSubscriptionRepository.findByEmail(any())).thenReturn(Optional.of(new NewsletterSubscription()));


        assertThrows(EmailSubscriptionAlreadyExistException.class, () -> newsletterService.joinNewsletter(subscriptionEmail));
    }

    @Test
    void testGetListOfSubscribers() {

        List<NewsletterSubscription> subscribers = List.of(new NewsletterSubscription("ibo@gmail.com"), new NewsletterSubscription("ibo@gmail.com"));
        when(newsletterSubscriptionRepository.findAll()).thenReturn(subscribers);


        NewsletterSubscribersResponseDto response = newsletterService.getListOfSubscribers();


        assertEquals("List of Subscribers", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(201, response.getStatus_code());
        assertEquals(subscribers, response.getNewsletterSubscribersData());
    }
}