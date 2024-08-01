package hng_java_boilerplate.blogCategory.unit_test;

import hng_java_boilerplate.blogCategory.controller.CategoryBlogController;
import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryRequestDTO;
import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryResponseDTO;
import hng_java_boilerplate.blogCategory.service.CreateBlogCategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryBlogControllerTest {

    @Mock
    private CreateBlogCategoryService createBlogCategoryService;

    @InjectMocks
    private CategoryBlogController categoryBlogController;

    @Test
    void testCreateBlogCategory() {

        CreateBlogCategoryRequestDTO requestDTO = new CreateBlogCategoryRequestDTO("Test Category");
        CreateBlogCategoryResponseDTO expectedResponseDTO = new CreateBlogCategoryResponseDTO("success", "Blog category created successfully.");

        when(createBlogCategoryService.blogCategoryRequestDTO(any(CreateBlogCategoryRequestDTO.class))).thenReturn(expectedResponseDTO);


        ResponseEntity<?> response = categoryBlogController.createBlogCategory(requestDTO);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponseDTO, response.getBody());
    }
}