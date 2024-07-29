package hng_java_boilerplate.testimonials.service;

import hng_java_boilerplate.testimonials.entity.Testimonial;
import hng_java_boilerplate.testimonials.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;

    public Testimonial createTestimonial(String userId, String name, String content) {
        Testimonial testimonial = new Testimonial();
        testimonial.setUserId(userId);
        testimonial.setName(name);
        testimonial.setContent(content);
        testimonial.setCreatedAt(LocalDate.now());

        return testimonialRepository.save(testimonial);
    }

    public Testimonial getTestimonialById(String testimonialId) {
        return testimonialRepository.findById(testimonialId).orElse(null);
    }
}
