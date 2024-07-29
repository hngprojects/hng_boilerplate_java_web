package blog_category_test.unit_test;

import hng_java_boilerplate.blogCategory.dto.CategoryBlogData;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateBlogCategoryServiceTest {

    @Mock
    private CreateBlogCategoryRepository categoryRepository;

    @InjectMocks
    private CreateBlogCategoryService createBlogCategoryService;

    @Test
    void testCreateBlogCategory() throws CategoryAlreadyExistsException {

        CreateBlogCategoryRequestDTO requestDTO = new CreateBlogCategoryRequestDTO("Test Category");
        CreateBlogCategoryResponseDTO expectedResponseDTO = new CreateBlogCategoryResponseDTO("success", "Blog category created successfully.", new CategoryBlogData(new Category(null, "Test Category", null)));
        when(categoryRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category("Test Category"));


        CreateBlogCategoryResponseDTO responseDTO = createBlogCategoryService.blogCategoryRequestDTO(requestDTO);


        assertEquals(expectedResponseDTO.getStatus(), responseDTO.getStatus());
        assertEquals(expectedResponseDTO.getMessage(), responseDTO.getMessage());
        assertEquals(expectedResponseDTO.getData().getName().getName(), responseDTO.getData().getName().getName());
    }

    @Test
    void testCreateBlogCategoryAlreadyExists() {

        CreateBlogCategoryRequestDTO requestDTO = new CreateBlogCategoryRequestDTO("Test Category");

        when(categoryRepository.findByName(any(String.class))).thenReturn(Optional.of(new Category("Test Category")));

// Act and Assert
        assertThrows(CategoryAlreadyExistsException.class, () -> createBlogCategoryService.blogCategoryRequestDTO(requestDTO));
    }
}