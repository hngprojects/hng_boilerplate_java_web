package hng_java_boilerplate.blogCategory.e2e_test;

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
    public void testCreateBlogCategorySuccess() throws CategoryAlreadyExistsException {
        // Arrange
        CreateBlogCategoryRequestDTO request = new CreateBlogCategoryRequestDTO("New Category");
        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.empty());

        // Act
        CreateBlogCategoryResponseDTO response = createBlogCategoryService.blogCategoryRequestDTO(request);

        // Assert
        assertEquals("success", response.getStatus());
        assertEquals("Blog category created successfully.", response.getMessage());
        assertEquals(request.getName(), response.getData().getName().getName());
    }

    @Test
    public void testCreateBlogCategoryFailureCategoryAlreadyExists() {
        // Arrange
        CreateBlogCategoryRequestDTO request = new CreateBlogCategoryRequestDTO("Existing Category");
        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.of(new Category()));

        // Act and Assert
        assertThrows(CategoryAlreadyExistsException.class, () -> createBlogCategoryService.blogCategoryRequestDTO(request));
    }
}