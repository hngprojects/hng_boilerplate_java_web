package hng_java_boilerplate.newsletter;


import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.newsletter.dto.UnsubscribeResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.repository.NewsletterRepository;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsletterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NewsletterRepository newsletterRepository;

    @InjectMocks
    private NewsletterService newsletterService;

    @Test
    public void testUnsubscribeFromNews_successfulUnsubscribe() {
        String subscriberId = "testId";
        User user = new User();
        user.setId(subscriberId);
        Newsletter subscriber = new Newsletter();
        subscriber.setId(subscriberId);

        when(userRepository.findById(subscriberId)).thenReturn(Optional.of(user));
        when(newsletterRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));

        UnsubscribeResponse response = newsletterService.unsubscribeFromNews(subscriberId);

        assertNull(response); // Or assert the expected response if not null
        verify(newsletterRepository, times(1)).delete(subscriber);
    }

    @Test
    public void testUnsubscribeFromNews_userNotFound() {
        String subscriberId = "nonExistentId";
        when(userRepository.findById(subscriberId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> newsletterService.unsubscribeFromNews(subscriberId));
        verify(newsletterRepository, never()).delete(any());
    }

    @Test
    public void testUnsubscribeFromNews_subscriberNotFound() {
        String subscriberId = "testId";
        User user = new User();
        user.setId(subscriberId);

        when(userRepository.findById(subscriberId)).thenReturn(Optional.of(user));
        when(newsletterRepository.findById(subscriberId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> newsletterService.unsubscribeFromNews(subscriberId));
        verify(newsletterRepository, never()).delete(any());
    }

//    @Test
//    public void testUnsubscribeFromNews_unauthorizedUser() {
//        String subscriberId = "testId";
//        User user = new User();
//        user.setId("differentId");
//        Newsletter subscriber = new Newsletter();
//        subscriber.setId(subscriberId);

//        when(userRepository.findById(subscriberId)).thenReturn(Optional.of(user));
//        when(newsletterRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));
//
//        assertThrows(UnAuthorizedException.class, () -> newsletterService.unsubscribeFromNews(subscriberId));
//        verify(newsletterRepository, never()).delete(any());
  //  }
}