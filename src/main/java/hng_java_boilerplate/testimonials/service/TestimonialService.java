package hng_java_boilerplate.testimonials.service;

import hng_java_boilerplate.testimonials.entity.Testimonial;
import hng_java_boilerplate.testimonials.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Page<Testimonial> getAllTestimonials(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return testimonialRepository.findAll(pageable);
    }

    public Testimonial updateTestimonial(String testimonialId, String userId, String content) {
        Testimonial testimonial = testimonialRepository.findById(testimonialId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Testimonial not found"));

        if (!testimonial.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only owners of testimonial can update");
        }

        testimonial.setContent(content);
        testimonial.setUpdatedAt(LocalDate.now());

        return testimonialRepository.save(testimonial);
    }
}
