package hng_java_boilerplate.about_page.controller;


import hng_java_boilerplate.about_page.controller.response.ErrorUpdateResponse;
import hng_java_boilerplate.about_page.controller.response.SuccessUpdateResponse;
import hng_java_boilerplate.about_page.entity.AboutPageDto;
import hng_java_boilerplate.about_page.service.AboutPageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.access.prepost.PreAuthorize;



@RequiredArgsConstructor
@RestController
public class AboutPageController {
    private final AboutPageService aboutPageService;

    @PutMapping("/api/v1/content/about/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAboutPage(@PathVariable Long id, @RequestBody AboutPageDto aboutPageDto) {
        try {
            aboutPageService.updateAboutPage(id, aboutPageDto);
            return ResponseEntity.ok(new SuccessUpdateResponse("About page content updated successfully.", 200));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorUpdateResponse("Failed to update About page content.", 404));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorUpdateResponse(e.getMessage(), 500));
            }
        }
    }
}
