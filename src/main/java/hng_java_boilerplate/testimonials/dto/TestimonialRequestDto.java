package hng_java_boilerplate.testimonials.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestimonialRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content must be at most 1000 characters")
    private String content;
}