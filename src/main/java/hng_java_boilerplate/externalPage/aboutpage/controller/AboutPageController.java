package hng_java_boilerplate.externalPage.aboutpage.controller;

import hng_java_boilerplate.externalPage.aboutpage.dto.AboutPageContentDto;
import hng_java_boilerplate.externalPage.aboutpage.dto.ApiResponse;
import hng_java_boilerplate.externalPage.aboutpage.service.AboutPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
public class AboutPageController {
    private final AboutPageService aboutPageService;

    @PutMapping("/about")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAboutPageContent(@Valid @RequestBody AboutPageContentDto contentDto) {
        try {
            aboutPageService.updateAboutPageContent(contentDto);
            return ResponseEntity.ok(new ApiResponse("About page content updated successfully.", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to update About page content.", 500));
        }
    }
}
