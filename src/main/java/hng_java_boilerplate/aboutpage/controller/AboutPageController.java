package hng_java_boilerplate.aboutpage.controller;

import hng_java_boilerplate.aboutpage.dto.AboutPageContentDto;
import hng_java_boilerplate.aboutpage.dto.ApiResponse;
import hng_java_boilerplate.aboutpage.service.AboutPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
@Tag(name="About Page", description = "controller for about page")
public class AboutPageController {
    private final AboutPageService aboutPageService;

    @PutMapping("/about")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Updates the about page content")
    public ResponseEntity<?> updateAboutPageContent(@Valid @RequestBody AboutPageContentDto contentDto) {
        aboutPageService.updateAboutPageContent(contentDto);
        return ResponseEntity.ok(new ApiResponse("About page content updated successfully.", 200));
    }

    @DeleteMapping("/about")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Deletes about page content")
    public ResponseEntity<?> deleteAboutPageContent() {
        aboutPageService.deleteAboutPageContent();
        return ResponseEntity.ok(new ApiResponse("About page content deleted successfully.", 200));
    }
}
