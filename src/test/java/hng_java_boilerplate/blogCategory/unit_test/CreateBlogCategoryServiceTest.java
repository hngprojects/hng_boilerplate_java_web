package hng_java_boilerplate.blogCategory.unit_test;

import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryRequestDTO;
import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryResponseDTO;
import hng_java_boilerplate.blogCategory.entity.Category;
import hng_java_boilerplate.blogCategory.exception.CategoryAlreadyExistsException;
import hng_java_boilerplate.blogCategory.repository.CreateBlogCategoryRepository;
import hng_java_boilerplate.blogCategory.service.CreateBlogCategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateBlogCategoryServiceTest {

    @Mock
    private CreateBlogCategoryRepository categoryRepository;

    @InjectMocks
    private CreateBlogCategoryService createBlogCategoryService;

    @Test
    public void testBlogCategoryRequestDTO_SUCCESS() throws CategoryAlreadyExistsException {
        // Arrange
        CreateBlogCategoryRequestDTO request = new CreateBlogCategoryRequestDTO();
        request.setName("Test Category");

        Category category = new Category();
        category.setName(request.getName());

        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(category)).thenReturn(category);

        // Act
        CreateBlogCategoryResponseDTO response = createBlogCategoryService.blogCategoryRequestDTO(request);

        // Assert
        assertEquals("success", response.getStatus());
        assertEquals("Blog category created successfully.", response.getMessage());
        assertEquals(request.getName(), response.getData().getName().getName());
    }

    @Test
    public void testBlogCategoryRequestDTO_CATEGORY_ALREADY_EXISTS() {
        // Arrange
        CreateBlogCategoryRequestDTO request = new CreateBlogCategoryRequestDTO();
        request.setName("Test Category");

        Category existingCategory = new Category();
        existingCategory.setName(request.getName());

        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.of(existingCategory));

        // Act and Assert
        assertThrows(CategoryAlreadyExistsException.class, () -> createBlogCategoryService.blogCategoryRequestDTO(request));
    }
}