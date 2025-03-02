package hng_java_boilerplate.newsletter.service;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.newsletter.dto.SubscribersResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.repository.NewsletterRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsletterServiceTest {

    @Mock
    private NewsletterRepository newsletterRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NewsletterService newsletterService;

    private Newsletter newsletter1;
    private Newsletter newsletter2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId("user1");
        user1.setEmail("user1@example.com");

        user2 = new User();
        user2.setId("user2");
        user2.setEmail("user2@example.com");

        newsletter1 = Newsletter.builder()
                .id("newsletter1")
                .userId("user1")
                .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        newsletter2 = Newsletter.builder()
                .id("newsletter2")
                .userId("user2")
                .createdAt(LocalDateTime.of(2021, 2, 15, 12, 30))
                .build();
    }

    @Test
    void getSubscribersResponse_shouldReturnCorrectResponse() {
        int page = 0;
        int size = 10;

        List<Newsletter> newsletters = List.of(newsletter1, newsletter2);
        Pageable pageable = PageRequest.of(page, size);
        Page<Newsletter> newsletterPage = new PageImpl<>(newsletters, pageable, newsletters.size());

        when(newsletterRepository.findAll(any(Pageable.class))).thenReturn(newsletterPage);
        when(userRepository.findById("user1")).thenReturn(Optional.of(user1));
        when(userRepository.findById("user2")).thenReturn(Optional.of(user2));

        SubscribersResponse response = newsletterService.getSubscribersResponse(page, size);

        assertNotNull(response);
        assertEquals(page, response.getPage());
        assertEquals(size, response.getSize());
        assertEquals(newsletters.size(), response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertNotNull(response.getSubscribers());
        assertEquals(2, response.getSubscribers().size());

        boolean foundNewsletter1 = response.getSubscribers().stream()
                .anyMatch(dto -> dto.getId().equals("newsletter1")
                        && dto.getEmail().equals("user1@example.com")
                        && dto.getSubscribedAt().equals(newsletter1.getCreatedAt()));
        boolean foundNewsletter2 = response.getSubscribers().stream()
                .anyMatch(dto -> dto.getId().equals("newsletter2")
                        && dto.getEmail().equals("user2@example.com")
                        && dto.getSubscribedAt().equals(newsletter2.getCreatedAt()));

        assertTrue(foundNewsletter1);
        assertTrue(foundNewsletter2);

        verify(newsletterRepository, times(1)).findAll(any(Pageable.class));
        verify(userRepository, times(1)).findById("user1");
        verify(userRepository, times(1)).findById("user2");
    }

    @Test
    void getSubscribersResponse_shouldThrowNotFoundException_whenUserNotFound() {
        int page = 0;
        int size = 10;

        List<Newsletter> newsletters = List.of(newsletter1);
        Pageable pageable = PageRequest.of(page, size);
        Page<Newsletter> newsletterPage = new PageImpl<>(newsletters, pageable, newsletters.size());

        when(newsletterRepository.findAll(any(Pageable.class))).thenReturn(newsletterPage);
        when(userRepository.findById("user1")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> newsletterService.getSubscribersResponse(page, size));

        assertEquals("User not found for subscription id: " + newsletter1.getId(), exception.getMessage());
        verify(newsletterRepository, times(1)).findAll(any(Pageable.class));
        verify(userRepository, times(1)).findById("user1");
    }
}
