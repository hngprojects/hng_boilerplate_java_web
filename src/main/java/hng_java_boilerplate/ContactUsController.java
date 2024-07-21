package hng_java_boilerplate;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hng_java_boilerplate.models.ContactUs;
import hng_java_boilerplate.services.GmailService;

@RestController
@RequestMapping("/api/v1/contact")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContactUsController {

    private final GmailService gmailService;
    private static final Logger logger = LoggerFactory.getLogger(ContactUsController.class);

    @Autowired
    public ContactUsController(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    @PostMapping
    public ResponseEntity<?> sendContactUsEmail(@Valid @RequestBody ContactUs contactUs, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Log the validation errors
            bindingResult.getAllErrors().forEach(error -> logger.error("Validation error: {}", error.getDefaultMessage()));
            // Return a bad request response with details about the validation errors
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Validation failed", 400));
        }
        try {
            gmailService.sendEmail(contactUs.getEmail(), contactUs.getName(), contactUs.getMessage());
            return ResponseEntity.ok().body(new ApiResponse(true, "Inquiry sent successfully", 200));
        } catch (IllegalArgumentException e) {
            logger.error("Bad Request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Bad Request", 400));
        } catch (Exception e) {
            logger.error("Internal Server Error: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse(false, "Internal Server Error", 500));
        }
    }
}