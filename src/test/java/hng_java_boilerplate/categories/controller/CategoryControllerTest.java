package hng_java_boilerplate.categories.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.categories.entity.Category;
import hng_java_boilerplate.categories.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void testCreateCategory() throws Exception {
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setSlug("test-category");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andExpect(jsonPath("$.slug").value("test-category"));
    }

    @Test
    void testGetAllCategories() throws Exception {
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setSlug("test-category");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        when(categoryService.getAllCategories()).thenReturn(List.of(category));

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Category"))
                .andExpect(jsonPath("$[0].slug").value("test-category"));
    }

}

