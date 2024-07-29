package hng_java_boilerplate.testimonials.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.product.errorhandler.ProductErrorHandler;
import hng_java_boilerplate.testimonials.dto.TestimonialRequestDto;
import hng_java_boilerplate.testimonials.entity.Testimonial;
import hng_java_boilerplate.testimonials.service.TestimonialService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TestimonialController.class)
@ExtendWith(MockitoExtension.class)
class TestimonialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestimonialService testimonialService;

    @MockBean
    private UserService userService;

    private User mockUser;

    @MockBean
    private ProductErrorHandler productErrorHandler;

    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId("userId123");
    }

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void createTestimonial_shouldReturnSuccess() throws Exception {
        when(userService.getLoggedInUser()).thenReturn(mockUser);

        TestimonialRequestDto requestDto = new TestimonialRequestDto();
        requestDto.setName("John Doe");
        requestDto.setContent("Great service!");

        Testimonial testimonial = new Testimonial();
        testimonial.setUserId(mockUser.getId());
        testimonial.setName(requestDto.getName());
        testimonial.setContent(requestDto.getContent());
        testimonial.setCreatedAt(LocalDate.now());

        when(testimonialService.createTestimonial(any(String.class), any(String.class), any(String.class)))
                .thenReturn(testimonial);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/v1/testimonials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Testimonial created successfully"))
                .andExpect(jsonPath("$.data.user_id").value("userId123"))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.content").value("Great service!"));
    }

    @Test
    void getTestimonialById_shouldReturnSuccess() throws Exception {
        when(userService.getLoggedInUser()).thenReturn(mockUser);

        Testimonial testimonial = new Testimonial();
        testimonial.setUserId("userId123");
        testimonial.setName("John Doe");
        testimonial.setContent("Great service!");
        testimonial.setCreatedAt(LocalDate.now());

        when(testimonialService.getTestimonialById("testimonialId123")).thenReturn(testimonial);

        mockMvc.perform(get("/api/v1/testimonials/testimonialId123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Testimonial fetched successfully"))
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.data.user_id").value("userId123"))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.content").value("Great service!"))
                .andExpect(jsonPath("$.data.created_at").value(testimonial.getCreatedAt().toString()));
    }

    @Test
    void getTestimonialById_shouldReturnNotFound() throws Exception {
        when(userService.getLoggedInUser()).thenReturn(mockUser);
        when(testimonialService.getTestimonialById("testimonialId123")).thenReturn(null);

        mockMvc.perform(get("/api/v1/testimonials/testimonialId123"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(404))
                .andExpect(jsonPath("$.message").value("Testimonial not found"));
    }
}
