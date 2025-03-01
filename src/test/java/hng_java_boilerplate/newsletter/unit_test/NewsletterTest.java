package hng_java_boilerplate.newsletter.unit_test;

import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.repository.NewsletterRepository;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import hng_java_boilerplate.user.dto.response.Response;
import hng_java_boilerplate.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class NewsletterTest {

    @InjectMocks
    private NewsletterService newsletterService;
    @Mock
    private NewsletterRepository newsletterRepository;

    private Newsletter newsletter1;
    private Newsletter newsletter2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        User user = new User();
        user.setId("U1");
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setCreatedAt(LocalDateTime.now());

        newsletter1 = new Newsletter();
        newsletter1.setUser(user);
        newsletter1.setCreatedAt(LocalDateTime.now());
        newsletter1.setId("1");
        newsletter1.setTitle("Newsletter test");
        newsletter1.setUpdatedAt(LocalDateTime.now());
        newsletter1.setContent("this a test content for the newsletter");

        newsletter2 = new Newsletter();
        newsletter1.setUser(user);
        newsletter2.setCreatedAt(LocalDateTime.now());
        newsletter2.setId("2");
        newsletter2.setUpdatedAt(LocalDateTime.now());
        newsletter2.setTitle("Newsletter test2");
        newsletter2.setContent("this a second test content for the newsletter");
    }

    @Test
    void testFindByUserId(){
        List<Newsletter> newsletters = Arrays.asList(newsletter1,newsletter2);
        Page<Newsletter> page = new PageImpl<>(newsletters);
        Pageable pageable = PageRequest.of(0,1);

        when(newsletterRepository.findByUser_Id("U1",pageable)).thenReturn(page);

        Page<Newsletter> result = newsletterService.findNewsletterByUserId(newsletter1.getUser().getId(),pageable);

        assertNotNull(result);
        assertEquals(1,result.getTotalPages());
        verify(newsletterRepository, times(1)).findByUser_Id(newsletter1.getUser().getId(),pageable);
    }

    @Test
    void testFindByCreatedAfter(){
        List<Newsletter> newsletters = Arrays.asList(newsletter1,newsletter2);
        Page<Newsletter> page = new PageImpl<>(newsletters,PageRequest.of(0,1),2);
        LocalDateTime date = LocalDateTime.parse("2025-02-28T11:44:32.180026100");
        when(newsletterRepository.findNewsletterByCreatedAtAfter(date,page.getPageable())).thenReturn(page);

        Page<Newsletter> result = newsletterService.findNewsletterByCreatedAtAfter(date,page.getPageable());

        assertNotNull(result);
        verify(newsletterRepository, times(1)).findNewsletterByCreatedAtAfter(date,page.getPageable());
    }

    @Test
    void testDeleteByUserId(){
        String userId = newsletter1.getUser().getId();

        Response<?> response = newsletterService.deleteNewsletterByUserId(userId);

        assertEquals("success", response.getStatus_code());
        assertEquals("Newsletter deleted successfully.", response.getMessage());
        verify(newsletterRepository, times(1)).deleteByUser_Id(userId);
    }
}
