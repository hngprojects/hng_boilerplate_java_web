package hng_java_boilerplate.newsletter.unit_test;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.newsletter.controller.NewsletterController;
import hng_java_boilerplate.newsletter.dto.NewsletterEmailRequestDto;
import hng_java_boilerplate.newsletter.dto.NewsletterEmailResponseDto;
import hng_java_boilerplate.newsletter.dto.NewsletterSubscribersResponseDto;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsletterControllerTest {

    @Mock
    private NewsletterService newsletterService;

    @Mock
    private EmailProducerService emailProducerService;

    @InjectMocks
    private NewsletterController newsletterController;

    @Test
    void testJoinNewsletter() {

        NewsletterEmailRequestDto requestDto = new NewsletterEmailRequestDto("test3@example.com");
        NewsletterEmailResponseDto responseDto = new NewsletterEmailResponseDto();
        when(newsletterService.joinNewsletter(any(NewsletterEmailRequestDto.class))).thenReturn(responseDto);


        ResponseEntity<?> response = newsletterController.joinNewsletter(requestDto);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(emailProducerService, times(1)).sendEmailMessage(anyString(), anyString(), anyString());
    }

    @Test
    void testGetListOfNewsletterSubscribers() {

        NewsletterSubscribersResponseDto responseDto = new NewsletterSubscribersResponseDto();
        when(newsletterService.getListOfSubscribers()).thenReturn(responseDto);


        ResponseEntity<NewsletterSubscribersResponseDto> response = newsletterController.getListOfNewsletterSubscribers();


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }
}