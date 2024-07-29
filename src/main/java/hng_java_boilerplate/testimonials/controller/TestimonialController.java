package hng_java_boilerplate.testimonials.controller;

import hng_java_boilerplate.testimonials.dto.TestimonialDataDto;
import hng_java_boilerplate.testimonials.dto.TestimonialRequestDto;
import hng_java_boilerplate.testimonials.dto.TestimonialResponseDto;
import hng_java_boilerplate.testimonials.entity.Testimonial;
import hng_java_boilerplate.testimonials.service.TestimonialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/testimonials")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;

    @PostMapping
    public ResponseEntity<?> createTestimonial(@AuthenticationPrincipal UserDetails userDetails,
                                               @Valid @RequestBody TestimonialRequestDto request) {
        Testimonial testimonial = testimonialService.createTestimonial(
                userDetails.getUsername(),
                request.getName(),
                request.getContent()
        );

        TestimonialDataDto testimonialData = new TestimonialDataDto(
                testimonial.getUserId(),
                testimonial.getName(),
                testimonial.getContent(),
                testimonial.getCreatedAt()
        );

        TestimonialResponseDto response = new TestimonialResponseDto("success", "Testimonial created successfully", testimonialData);

        return ResponseEntity.ok(response);
    }
}
