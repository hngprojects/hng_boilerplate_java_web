package hng_java_boilerplate.testimonials.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.testimonials.dto.TestimonialRequestDto;
import hng_java_boilerplate.testimonials.dto.UpdateTestimonialRequestDto;
import hng_java_boilerplate.testimonials.entity.Testimonial;
import hng_java_boilerplate.testimonials.service.TestimonialService;
import hng_java_boilerplate.authentication.user.entity.User;
import hng_java_boilerplate.authentication.user.service.UserService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

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
        testimonial.setUser_id(mockUser.getId());
        testimonial.setName(requestDto.getName());
        testimonial.setContent(requestDto.getContent());
        testimonial.setCreated_at(LocalDate.now());

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
        testimonial.setUser_id("userId123");
        testimonial.setName("John Doe");
        testimonial.setContent("Great service!");
        testimonial.setCreated_at(LocalDate.now());

        when(testimonialService.getTestimonialById("testimonialId123")).thenReturn(testimonial);

        mockMvc.perform(get("/api/v1/testimonials/testimonialId123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Testimonial fetched successfully"))
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.data.user_id").value("userId123"))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.content").value("Great service!"))
                .andExpect(jsonPath("$.data.created_at").value(testimonial.getCreated_at().toString()));
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
        testimonial1.setUser_id("userId1");
        testimonial1.setName("John Doe");
        testimonial1.setContent("Great service!");
        testimonial1.setCreated_at(LocalDate.now());

        Testimonial testimonial2 = new Testimonial();
        testimonial2.setUser_id("userId2");
        testimonial2.setName("Jane Smith");
        testimonial2.setContent("Excellent support!");
        testimonial2.setCreated_at(LocalDate.now());

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
                .andExpect(jsonPath("$.data[0].created_at").value(testimonial1.getCreated_at().toString()))
                .andExpect(jsonPath("$.data[1].user_id").value("userId2"))
                .andExpect(jsonPath("$.data[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$.data[1].content").value("Excellent support!"))
                .andExpect(jsonPath("$.data[1].created_at").value(testimonial2.getCreated_at().toString()))
                .andExpect(jsonPath("$.pagination.current_page").value(1))
                .andExpect(jsonPath("$.pagination.per_page").value(3))
                .andExpect(jsonPath("$.pagination.total_pages").value(1))
                .andExpect(jsonPath("$.pagination.total_testimonials").value(2));
    }

    @Test
    void shouldUpdateTestimonial() throws Exception {
        User mockUser = new User();
        mockUser.setId("userId123");

        when(userService.getLoggedInUser()).thenReturn(mockUser);

        Testimonial testimonial = new Testimonial();
        testimonial.setUser_id("userId123");
        testimonial.setName("John Doe");
        testimonial.setContent("Updated content");
        testimonial.setCreated_at(LocalDate.now());
        testimonial.setUpdated_at(LocalDate.now());

        when(testimonialService.updateTestimonial("testimonialId123", "userId123", "Updated content")).thenReturn(testimonial);

        UpdateTestimonialRequestDto request = new UpdateTestimonialRequestDto();
        request.setContent("Updated content");

        mockMvc.perform(patch("/api/v1/testimonials/testimonialId123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Testimonial updated successfully"))
                .andExpect(jsonPath("$.data.user_id").value("userId123"))
                .andExpect(jsonPath("$.data.content").value("Updated content"))
                .andExpect(jsonPath("$.data.updated_at").value(testimonial.getUpdated_at().toString()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTestimonial_shouldReturnSuccess() throws Exception {
        when(userService.getLoggedInUser()).thenReturn(mockUser);

        mockMvc.perform(delete("/api/v1/testimonials/testimonialId123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Testimonial deleted successfully"))
                .andExpect(jsonPath("$.status_code").value(200));
    }

    @Test
    void deleteTestimonial_shouldReturnUnauthorizedWhenUserNotAuthenticated() throws Exception {
        when(userService.getLoggedInUser()).thenReturn(null);

        mockMvc.perform(delete("/api/v1/testimonials/testimonialId123"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("User not authenticated"))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.status_code").value(401));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTestimonial_shouldReturnNotFoundWhenTestimonialDoesNotExist() throws Exception {
        when(userService.getLoggedInUser()).thenReturn(mockUser);
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Testimonial not found"))
                .when(testimonialService).deleteTestimonial("testimonialId123");

        mockMvc.perform(delete("/api/v1/testimonials/testimonialId123"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Testimonial not found"))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.status_code").value(404));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTestimonial_shouldReturnInternalServerErrorOnUnexpectedException() throws Exception {
        when(userService.getLoggedInUser()).thenReturn(mockUser);
        doThrow(new RuntimeException("Unexpected error"))
                .when(testimonialService).deleteTestimonial("testimonialId123");

        mockMvc.perform(delete("/api/v1/testimonials/testimonialId123"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("An error occurred while deleting the testimonial"))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status_code").value(500));
    }
}