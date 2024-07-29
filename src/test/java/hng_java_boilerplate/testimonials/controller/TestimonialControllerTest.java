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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

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

    @Test
    void getAllTestimonials_shouldReturnSuccess() throws Exception {
        when(userService.getLoggedInUser()).thenReturn(mockUser);

        Testimonial testimonial1 = new Testimonial();
        testimonial1.setUserId("userId1");
        testimonial1.setName("John Doe");
        testimonial1.setContent("Great service!");
        testimonial1.setCreatedAt(LocalDate.now());

        Testimonial testimonial2 = new Testimonial();
        testimonial2.setUserId("userId2");
        testimonial2.setName("Jane Smith");
        testimonial2.setContent("Excellent support!");
        testimonial2.setCreatedAt(LocalDate.now());

        List<Testimonial> testimonials = List.of(testimonial1, testimonial2);
        Page<Testimonial> testimonialPage = new PageImpl<>(testimonials, PageRequest.of(0, 3), testimonials.size());

        when(testimonialService.getAllTestimonials(1, 3)).thenReturn(testimonialPage);

        mockMvc.perform(get("/api/v1/testimonials?page=1&size=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Testimonials retrieved successfully"))
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.data[0].user_id").value("userId1"))
                .andExpect(jsonPath("$.data[0].name").value("John Doe"))
                .andExpect(jsonPath("$.data[0].content").value("Great service!"))
                .andExpect(jsonPath("$.data[0].created_at").value(testimonial1.getCreatedAt().toString()))
                .andExpect(jsonPath("$.data[1].user_id").value("userId2"))
                .andExpect(jsonPath("$.data[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$.data[1].content").value("Excellent support!"))
                .andExpect(jsonPath("$.data[1].created_at").value(testimonial2.getCreatedAt().toString()))
                .andExpect(jsonPath("$.pagination.current_page").value(1))
                .andExpect(jsonPath("$.pagination.per_page").value(3))
                .andExpect(jsonPath("$.pagination.total_pages").value(1))
                .andExpect(jsonPath("$.pagination.total_testimonials").value(2));
    }
}
