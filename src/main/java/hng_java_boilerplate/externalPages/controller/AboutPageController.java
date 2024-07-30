package hng_java_boilerplate.externalPages.controller;

import hng_java_boilerplate.externalPages.dtos.AboutPageDTO;
import hng_java_boilerplate.externalPages.dtos.RetrievalDTO;
import hng_java_boilerplate.externalPages.services.AboutPageService;
import hng_java_boilerplate.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/content/about")
public class AboutPageController {
    private final AboutPageService aboutPageService;
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ResponseEntity<?> getAboutPage(Authentication authentication){
        if (authentication == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "invalid authentication credentials"));
        }
        if (!isUserAuthorized(authentication)){
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "you do not have the necessary permission to access this resource"));
        }
       try{
           RetrievalDTO aboutPageDTO = aboutPageService.getAboutPage();
           return ResponseEntity.ok(aboutPageDTO);
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "failed to retrieve about page content"));
       }
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ResponseEntity<?> updateAboutPage(@RequestBody AboutPageDTO aboutPageDTO, Authentication authentication){
        if (authentication == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "invalid authentication credentials"));
        }
        if (!isUserAuthorized(authentication)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "you do not have the necessary permission to access this resource"));
        }
        try{
            AboutPageDTO updatedAboutPageDTO = aboutPageService.updateAboutPage(aboutPageDTO);
            return ResponseEntity.ok(Collections.singletonMap("message", "about page updated successfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "failed to update about page content"));

        }
    }

    private boolean isUserAuthorized(Authentication authentication) {
        if (authentication == null) {

            return false;
        }
        String adminRole = Role.ROLE_ADMIN.name();
        return authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(adminRole));
    }
}
