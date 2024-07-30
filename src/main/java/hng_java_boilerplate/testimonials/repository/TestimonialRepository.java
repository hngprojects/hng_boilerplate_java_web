package hng_java_boilerplate.testimonials.repository;

import hng_java_boilerplate.testimonials.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonial, String> {
}
