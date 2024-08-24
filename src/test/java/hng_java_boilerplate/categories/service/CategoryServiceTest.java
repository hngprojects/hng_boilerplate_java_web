package hng_java_boilerplate.categories.service;

import hng_java_boilerplate.categories.dto.CategoryDto;
import hng_java_boilerplate.categories.dto.CategoryRequest;
import hng_java_boilerplate.categories.entity.Category;
import hng_java_boilerplate.categories.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

        CategoryRequest request = new CategoryRequest();
        request.setName(category.getName());
        CategoryDto createdCategory = categoryService.createCategory(request);

        assertNotNull(createdCategory);
        assertEquals("Test Category", createdCategory.getName());
        assertNotNull(createdCategory.getCreated_at());
        assertNotNull(createdCategory.getUpdated_at());
        verify(categoryRepository, times(1)).saveAndFlush(any(Category.class));
    }

    @Test
    void getAllCategories() {
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDto> result = categoryService.getAllCategories();

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryDto result = categoryService.getCategoryById(id);

        assertEquals(category.getId(), result.getCategory_id());
        assertEquals(category.getName(), result.getName());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void deleteCategory() {
        UUID id = UUID.randomUUID();
        Category category = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(id);

        verify(categoryRepository, times(1)).delete(category);
    }
}