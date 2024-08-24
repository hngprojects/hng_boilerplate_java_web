package hng_java_boilerplate.testimonials.controller;

import hng_java_boilerplate.testimonials.dto.TestimonialDataDto;
import hng_java_boilerplate.testimonials.dto.TestimonialRequestDto;
import hng_java_boilerplate.testimonials.dto.TestimonialResponseDto;
import hng_java_boilerplate.testimonials.dto.UpdateTestimonialRequestDto;
import hng_java_boilerplate.testimonials.entity.Testimonial;
import hng_java_boilerplate.testimonials.service.TestimonialService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/testimonials")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createTestimonial(@Valid @RequestBody TestimonialRequestDto request) {
        User loggedInUser = userService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        Testimonial testimonial = testimonialService.createTestimonial(loggedInUser.getId(), request.getName(), request.getContent());

        TestimonialDataDto testimonialData = new TestimonialDataDto(testimonial.getUser_id(), testimonial.getName(), testimonial.getContent(), testimonial.getCreated_at(), testimonial.getUpdated_at());

        TestimonialResponseDto response = new TestimonialResponseDto("success", "Testimonial created successfully", testimonialData);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{testimonial_id}")
    public ResponseEntity<?> getTestimonialById(@PathVariable("testimonial_id") String testimonialId) {
        User loggedInUser = userService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        Testimonial testimonial = testimonialService.getTestimonialById(testimonialId);
        if (testimonial == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status_code", 404, "message", "Testimonial not found"));
        }

        TestimonialDataDto testimonialData = new TestimonialDataDto(testimonial.getUser_id(), testimonial.getName(), testimonial.getContent(), testimonial.getCreated_at(), testimonial.getUpdated_at());
        return ResponseEntity.ok(Map.of("message", "Testimonial fetched successfully", "status_code", 200, "data", testimonialData));
    }

    @GetMapping
    public ResponseEntity<?> getAllTestimonials(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        User loggedInUser = userService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        Page<Testimonial> testimonialsPage = testimonialService.getAllTestimonials(page, size);
        List<TestimonialDataDto> testimonials = testimonialsPage.getContent().stream()
                .map(testimonial -> new TestimonialDataDto(testimonial.getUser_id(), testimonial.getName(), testimonial.getContent(), testimonial.getCreated_at(), testimonial.getUpdated_at()))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Testimonials retrieved successfully");
        response.put("status_code", 200);
        response.put("data", testimonials);
        response.put("pagination", Map.of(
                "current_page", testimonialsPage.getNumber() + 1,
                "per_page", testimonialsPage.getSize(),
                "total_pages", testimonialsPage.getTotalPages(),
                "total_testimonials", testimonialsPage.getTotalElements()
        ));

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{testimonial_id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateTestimonial(@PathVariable("testimonial_id") String testimonialId,
                                               @Valid @RequestBody UpdateTestimonialRequestDto request) {
        User loggedInUser = userService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        Testimonial testimonial = testimonialService.updateTestimonial(testimonialId, loggedInUser.getId(), request.getContent());

        TestimonialDataDto testimonialData = new TestimonialDataDto(testimonial.getUser_id(), testimonial.getName(), testimonial.getContent(), testimonial.getUpdated_at(), testimonial.getCreated_at());

        TestimonialResponseDto response = new TestimonialResponseDto("success", "Testimonial updated successfully", testimonialData);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{testimonial_id}")
    @Secured("ROLE_SUPER_ADMIN")
    public ResponseEntity<?> deleteTestimonial(@PathVariable("testimonial_id") String testimonialId) {
        User loggedInUser = userService.getLoggedInUser();

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "User not authenticated", "error", "Unauthorized", "status_code", 401));
        }

        try {
            testimonialService.deleteTestimonial(testimonialId);
            return ResponseEntity.ok(Map.of(
                    "success", true, "message", "Testimonial deleted successfully", "status_code", 200));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                    "message", Objects.requireNonNull(ex.getReason()), "error", ex.getStatusCode(), "status_code", ex.getStatusCode().value()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "An error occurred while deleting the testimonial", "error", "Internal Server Error", "status_code", 500));
        }
    }

}