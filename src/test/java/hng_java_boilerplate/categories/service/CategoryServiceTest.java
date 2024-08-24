package hng_java_boilerplate.categories.service;

import hng_java_boilerplate.categories.entity.Category;
import hng_java_boilerplate.categories.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory() {
        Category category = new Category();
        category.setName("Test Category");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.createCategory(category);

        assertNotNull(createdCategory);
        assertEquals("Test Category", createdCategory.getName());
        assertNotNull(createdCategory.getCreatedAt());
        assertNotNull(createdCategory.getUpdatedAt());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void getAllCategories() {
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(categories, result);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryById(id);

        assertTrue(result.isPresent());
        assertEquals(category, result.get());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void deleteCategory() {
        UUID id = UUID.randomUUID();

        categoryService.deleteCategory(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }
}