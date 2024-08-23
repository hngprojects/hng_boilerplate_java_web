package hng_java_boilerplate.waitlist.controller;

import hng_java_boilerplate.blog.waitlist.controller.WaitlistPageController;
import hng_java_boilerplate.blog.waitlist.entity.WaitlistPage;
import hng_java_boilerplate.blog.waitlist.service.WaitlistPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WaitlistPageControllerTest {

    @Mock
    private WaitlistPageService waitlistPageService;

    @InjectMocks
    private WaitlistPageController waitlistPageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateWaitlistPage() throws IOException, SQLException {
        WaitlistPage waitlistPage = new WaitlistPage();
        waitlistPage.setId(UUID.randomUUID());

        when(waitlistPageService.createWaitlistPage(any(WaitlistPage.class))).thenReturn(waitlistPage);

        ResponseEntity<?> response = waitlistPageController.createWaitlistPage(waitlistPage);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Waitlist page created successfully", responseBody.get("message"));
        assertEquals(HttpStatus.CREATED.value(), responseBody.get("status_code"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetWaitlistPages() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WaitlistPage> waitlistPagePage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(waitlistPageService.getWaitlistPages(pageable)).thenReturn(waitlistPagePage);

        ResponseEntity<?> response = waitlistPageController.getWaitlistPages(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Retrieved waitlist pages successfully", responseBody.get("message"));
        assertEquals(HttpStatus.OK.value(), responseBody.get("status_code"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSearchWaitlistPages() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WaitlistPage> waitlistPagePage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(waitlistPageService.searchWaitlistPages(anyString(), any(Pageable.class))).thenReturn(waitlistPagePage);

        ResponseEntity<?> response = waitlistPageController.searchWaitlistPages("keyword", pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Search completed successfully", responseBody.get("message"));
        assertEquals(HttpStatus.OK.value(), responseBody.get("status_code"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testToggleWaitlistPageActive() {
        WaitlistPage waitlistPage = new WaitlistPage();
        waitlistPage.setId(UUID.randomUUID());
        waitlistPage.setActive(true);

        when(waitlistPageService.toggleWaitlistPageActive(any(UUID.class))).thenReturn(waitlistPage);

        ResponseEntity<?> response = waitlistPageController.toggleWaitlistPageActive(waitlistPage.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Waitlist page active status toggled successfully", responseBody.get("message"));
        assertEquals(waitlistPage.getId(), responseBody.get("id"));
        assertEquals(waitlistPage.isActive(), responseBody.get("active"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteWaitlistPageSuccess() {
        when(waitlistPageService.deleteWaitlistPage(anyString())).thenReturn(true);

        ResponseEntity<?> response = waitlistPageController.deleteWaitlistPage("some-id");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Waitlist page deleted successfully", responseBody.get("message"));
        assertEquals(200, responseBody.get("statusCode"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteWaitlistPageNotFound() {
        when(waitlistPageService.deleteWaitlistPage(anyString())).thenReturn(false);

        ResponseEntity<?> response = waitlistPageController.deleteWaitlistPage("some-id");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Waitlist page not found", responseBody.get("message"));
        assertEquals(404, responseBody.get("statusCode"));
    }
}
