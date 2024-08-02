package hng_java_boilerplate.newsletter.unit_test;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsletterServiceTest {

    @Mock
    private NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    @InjectMocks
    private NewsletterService newsletterService;

    @Test
    void testJoinNewsletterSuccess() throws EmailSubscriptionAlreadyExistException {

        NewsletterEmailRequestDto requestDto = new NewsletterEmailRequestDto("test@example.com");
        when(newsletterSubscriptionRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());


        NewsletterEmailResponseDto responseDto = newsletterService.joinNewsletter(requestDto);


        assertEquals("Your Subscription was Successful", responseDto.getMessage());
        assertEquals(true, responseDto.isSuccess());
        assertEquals(201, responseDto.getStatus_code());
    }

    @Test
    void testJoinNewsletterEmailAlreadyExists() {

        NewsletterEmailRequestDto requestDto = new NewsletterEmailRequestDto("test@example.com");
        when(newsletterSubscriptionRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(new NewsletterSubscription()));


        assertThrows(EmailSubscriptionAlreadyExistException.class, () -> newsletterService.joinNewsletter(requestDto));
    }

    @Test
    void testGetListOfSubscribers() {

        List<NewsletterSubscription> subscribers = List.of(new NewsletterSubscription());
        when(newsletterSubscriptionRepository.findAll()).thenReturn(subscribers);


        NewsletterSubscribersResponseDto responseDto = newsletterService.getListOfSubscribers();


        assertEquals("List of Subscribers", responseDto.getMessage());
        assertEquals(true, responseDto.isSuccess());
        assertEquals(201, responseDto.getStatus_code());
        assertEquals(subscribers, responseDto.getNewsletterSubscribersData());
    }
}