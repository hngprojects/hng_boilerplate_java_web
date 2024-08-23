package hng_java_boilerplate.squeeze.controller;

import hng_java_boilerplate.blog.squeeze.controller.SqueezeConfigController;
import hng_java_boilerplate.blog.squeeze.entity.SqueezeConfig;
import hng_java_boilerplate.blog.squeeze.service.SqueezeConfigService;
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

public class SqueezeConfigControllerTest {

    @Mock
    private SqueezeConfigService squeezeConfigService;

    @InjectMocks
    private SqueezeConfigController squeezeConfigController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSqueezePage() throws IOException, SQLException {
        SqueezeConfig squeezeConfig = new SqueezeConfig();
        squeezeConfig.setId(UUID.randomUUID());

        when(squeezeConfigService.createSqueezePage(any(SqueezeConfig.class))).thenReturn(squeezeConfig);

        ResponseEntity<?> response = squeezeConfigController.createSqueezePage(squeezeConfig);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Squeeze config page created successfully", responseBody.get("message"));
        assertEquals(HttpStatus.CREATED.value(), responseBody.get("status_code"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetSqueezePages() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SqueezeConfig> squeezeConfigPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(squeezeConfigService.getSqueezePages(pageable)).thenReturn(squeezeConfigPage);

        ResponseEntity<?> response = squeezeConfigController.getSqueezePages(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Retrieved squeeze pages successfully", responseBody.get("message"));
        assertEquals(HttpStatus.OK.value(), responseBody.get("status_code"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSearchSqueezePages() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SqueezeConfig> squeezeConfigPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(squeezeConfigService.searchSqueezePages(anyString(), any(Pageable.class))).thenReturn(squeezeConfigPage);

        ResponseEntity<?> response = squeezeConfigController.searchSqueezePages("keyword", pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Search completed successfully", responseBody.get("message"));
        assertEquals(HttpStatus.OK.value(), responseBody.get("status_code"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testToggleSqueezePageActive() {
        SqueezeConfig squeezeConfig = new SqueezeConfig();
        squeezeConfig.setId(UUID.randomUUID());
        squeezeConfig.setActive(true);

        when(squeezeConfigService.toggleSqueezePageActive(any(UUID.class))).thenReturn(squeezeConfig);

        ResponseEntity<?> response = squeezeConfigController.toggleSqueezePageActive(squeezeConfig.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Squeeze page active status toggled successfully", responseBody.get("message"));
        assertEquals(squeezeConfig.getId(), responseBody.get("id"));
        assertEquals(squeezeConfig.isActive(), responseBody.get("active"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteSqueezePageSuccess() {
        when(squeezeConfigService.deleteSqueezePage(anyString())).thenReturn(true);

        ResponseEntity<?> response = squeezeConfigController.deleteSqueezePage("some-id");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Squeeze page deleted successfully", responseBody.get("message"));
        assertEquals(200, responseBody.get("statusCode"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteSqueezePageNotFound() {
        when(squeezeConfigService.deleteSqueezePage(anyString())).thenReturn(false);

        ResponseEntity<?> response = squeezeConfigController.deleteSqueezePage("some-id");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Squeeze page not found", responseBody.get("message"));
        assertEquals(404, responseBody.get("statusCode"));
    }
}