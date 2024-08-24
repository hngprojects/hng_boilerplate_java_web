package hng_java_boilerplate.blogCategory.unit_test;

import hng_java_boilerplate.blogCategory.dto.BlogCategoryRequestDTO;
import hng_java_boilerplate.blogCategory.dto.BlogCategoryResponseDto;
import hng_java_boilerplate.blogCategory.entity.BlogCategory;
import hng_java_boilerplate.blogCategory.exception.BlogCategoryAlreadyExistsException;
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
    public void testBlogCategoryRequestDTO_SUCCESS() throws BlogCategoryAlreadyExistsException {
        BlogCategoryRequestDTO request = new BlogCategoryRequestDTO();
        request.setName("Test Category");

        BlogCategory category = new BlogCategory();
        category.setName(request.getName());

        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(category)).thenReturn(category);


        BlogCategoryResponseDto response = createBlogCategoryService.CreateBlogCategoryRequestDTO(request);

        assertEquals("success", response.getStatus());
        assertEquals("Blog category created successfully.", response.getMessage());
        assertEquals(request.getName(), response.getData().getName().getName());
    }

    @Test
    public void testBlogCategoryRequestDTO_CATEGORY_ALREADY_EXISTS() {

        BlogCategoryRequestDTO request = new BlogCategoryRequestDTO();
        request.setName("Test Category");

        BlogCategory existingCategory = new BlogCategory();
        existingCategory.setName(request.getName());

        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.of(existingCategory));
        assertThrows(BlogCategoryAlreadyExistsException.class, () -> createBlogCategoryService.CreateBlogCategoryRequestDTO(request));
    }
}