package hng_java_boilerplate.testimonials.service;

import hng_java_boilerplate.testimonials.entity.Testimonial;
import hng_java_boilerplate.testimonials.repository.TestimonialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TestimonialServiceTest {

    @InjectMocks
    private TestimonialService testimonialService;

    @Mock
    private TestimonialRepository testimonialRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTestimonial_shouldSaveAndReturnTestimonial() {
        Testimonial testimonial = new Testimonial();
        testimonial.setUserId("userId123");
        testimonial.setName("John Doe");
        testimonial.setContent("Great service!");
        testimonial.setCreatedAt(LocalDate.now());

        when(testimonialRepository.save(any(Testimonial.class))).thenReturn(testimonial);

        Testimonial result = testimonialService.createTestimonial("userId123", "John Doe", "Great service!");

        assertEquals("userId123", result.getUserId());
        assertEquals("John Doe", result.getName());
        assertEquals("Great service!", result.getContent());
        assertEquals(LocalDate.now(), result.getCreatedAt());
    }
}
