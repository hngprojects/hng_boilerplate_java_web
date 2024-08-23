package hng_java_boilerplate.squeeze.controller;

import hng_java_boilerplate.squeeze.entity.SqueezeConfig;
import hng_java_boilerplate.squeeze.service.SqueezeConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/squeeze/config")
@Tag(name = "Squeeze Pages")
public class SqueezeConfigController {

    private final SqueezeConfigService squeezeConfigService;

    public SqueezeConfigController(SqueezeConfigService squeezeConfigService) {
        this.squeezeConfigService = squeezeConfigService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createSqueezePage(@RequestBody SqueezeConfig squeezeConfig) throws IOException, SQLException {
        SqueezeConfig savedPage = squeezeConfigService.createSqueezePage(squeezeConfig);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Squeeze config page created successfully");
        response.put("id", savedPage.getId());
        response.put("status_code", HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getSqueezePages(Pageable pageable) {
        Page<SqueezeConfig> squeezePages = squeezeConfigService.getSqueezePages(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("pages", squeezePages.getContent());
        response.put("current_page", squeezePages.getNumber());
        response.put("total_items", squeezePages.getTotalElements());
        response.put("total_pages", squeezePages.getTotalPages());
        response.put("status_code", HttpStatus.OK.value());
        response.put("message", "Retrieved squeeze pages successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSqueezePages(@RequestParam String keyword, Pageable pageable) {
        Page<SqueezeConfig> squeezePages = squeezeConfigService.searchSqueezePages(keyword, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("pages", squeezePages.getContent());
        response.put("current_page", squeezePages.getNumber());
        response.put("total_items", squeezePages.getTotalElements());
        response.put("total_pages", squeezePages.getTotalPages());
        response.put("status_code", HttpStatus.OK.value());
        response.put("message", "Search completed successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<?> toggleSqueezePageActive(@PathVariable UUID id) {
        SqueezeConfig updatedPage = squeezeConfigService.toggleSqueezePageActive(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Squeeze page active status toggled successfully");
        response.put("id", updatedPage.getId());
        response.put("active", updatedPage.isActive());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteSqueezePage(@PathVariable String id) {
        boolean deleted = squeezeConfigService.deleteSqueezePage(id);

        Map<String, Object> response = new HashMap<>();

        if (deleted) {
            response.put("message", "Squeeze page deleted successfully");
            response.put("statusCode", 200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Squeeze page not found");
            response.put("statusCode", 404);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
