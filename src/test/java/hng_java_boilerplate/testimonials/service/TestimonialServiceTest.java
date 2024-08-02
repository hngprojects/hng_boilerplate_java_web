package hng_java_boilerplate.testimonials.service;

import hng_java_boilerplate.testimonials.entity.Testimonial;
import hng_java_boilerplate.testimonials.repository.TestimonialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void getTestimonialById_shouldReturnTestimonialWhenFound() {
        Testimonial testimonial = new Testimonial();
        testimonial.setId("testimonialId123");
        testimonial.setUserId("userId123");
        testimonial.setName("John Doe");
        testimonial.setContent("Great service!");
        testimonial.setCreatedAt(LocalDate.now());

        when(testimonialRepository.findById("testimonialId123")).thenReturn(Optional.of(testimonial));

        Testimonial result = testimonialService.getTestimonialById("testimonialId123");

        assertEquals("testimonialId123", result.getId());
        assertEquals("userId123", result.getUserId());
        assertEquals("John Doe", result.getName());
        assertEquals("Great service!", result.getContent());
    }

    @Test
    void getTestimonialById_shouldReturnNullWhenNotFound() {
        when(testimonialRepository.findById("testimonialId123")).thenReturn(Optional.empty());

        Testimonial result = testimonialService.getTestimonialById("testimonialId123");

        assertNull(result);
    }

    @Test
    void getAllTestimonials_shouldReturnPagedTestimonials() {
        Testimonial testimonial1 = new Testimonial();
        testimonial1.setId("testimonialId1");
        testimonial1.setUserId("userId1");
        testimonial1.setName("John Doe");
        testimonial1.setContent("Great service!");
        testimonial1.setCreatedAt(LocalDate.now());

        Testimonial testimonial2 = new Testimonial();
        testimonial2.setId("testimonialId2");
        testimonial2.setUserId("userId2");
        testimonial2.setName("Jane Doe");
        testimonial2.setContent("Excellent service!");
        testimonial2.setCreatedAt(LocalDate.now());

        Page<Testimonial> page = new PageImpl<>(Arrays.asList(testimonial1, testimonial2), PageRequest.of(0, 2), 2);

        when(testimonialRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);

        Page<Testimonial> result = testimonialService.getAllTestimonials(1, 2);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("testimonialId1", result.getContent().get(0).getId());
        assertEquals("testimonialId2", result.getContent().get(1).getId());
    }

    @Test
    void updateTestimonial_shouldUpdateAndReturnTestimonial() {
        Testimonial testimonial = new Testimonial();
        testimonial.setId("testimonialId123");
        testimonial.setUserId("userId123");
        testimonial.setName("John Doe");
        testimonial.setContent("Great service!");
        testimonial.setCreatedAt(LocalDate.now());

        when(testimonialRepository.findById("testimonialId123")).thenReturn(Optional.of(testimonial));
        when(testimonialRepository.save(any(Testimonial.class))).thenReturn(testimonial);

        Testimonial result = testimonialService.updateTestimonial("testimonialId123", "userId123", "Updated content");

        assertEquals("testimonialId123", result.getId());
        assertEquals("userId123", result.getUserId());
        assertEquals("Updated content", result.getContent());
        assertEquals(LocalDate.now(), result.getUpdatedAt());
    }

    @Test
    void updateTestimonial_shouldThrowExceptionWhenNotFound() {
        when(testimonialRepository.findById("testimonialId123")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            testimonialService.updateTestimonial("testimonialId123", "userId123", "Updated content");
        });
    }

    @Test
    void updateTestimonial_shouldThrowExceptionWhenUserNotAuthorized() {
        Testimonial testimonial = new Testimonial();
        testimonial.setId("testimonialId123");
        testimonial.setUserId("anotherUserId");
        testimonial.setName("John Doe");
        testimonial.setContent("Great service!");
        testimonial.setCreatedAt(LocalDate.now());

        when(testimonialRepository.findById("testimonialId123")).thenReturn(Optional.of(testimonial));

        assertThrows(ResponseStatusException.class, () -> {
            testimonialService.updateTestimonial("testimonialId123", "userId123", "Updated content");
        });
    }
}