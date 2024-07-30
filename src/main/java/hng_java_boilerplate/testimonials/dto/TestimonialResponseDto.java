package hng_java_boilerplate.testimonials.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TestimonialResponseDto {
    private String status;
    private String message;
    private TestimonialDataDto data;
}
