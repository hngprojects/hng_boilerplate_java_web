package hng_java_boilerplate.testimonials.controller;

import hng_java_boilerplate.testimonials.dto.TestimonialDataDto;
import hng_java_boilerplate.testimonials.dto.TestimonialRequestDto;
import hng_java_boilerplate.testimonials.dto.TestimonialResponseDto;
import hng_java_boilerplate.testimonials.entity.Testimonial;
import hng_java_boilerplate.testimonials.service.TestimonialService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/testimonials")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createTestimonial(@Valid @RequestBody TestimonialRequestDto request) {
        User loggedInUser = userService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        Testimonial testimonial = testimonialService.createTestimonial(loggedInUser.getId(), request.getName(), request.getContent());

        TestimonialDataDto testimonialData = new TestimonialDataDto(testimonial.getUserId(), testimonial.getName(), testimonial.getContent(), testimonial.getCreatedAt());

        TestimonialResponseDto response = new TestimonialResponseDto("success", "Testimonial created successfully", testimonialData);

        return ResponseEntity.ok(response);
    }
}
