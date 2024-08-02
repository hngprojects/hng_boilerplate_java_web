package hng_java_boilerplate.blogCategory.controller;

import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryRequestDTO;
import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryResponseDTO;
import hng_java_boilerplate.blogCategory.service.CreateBlogCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/create")
public class CategoryBlogController {

    private final CreateBlogCategoryService createBlogCategoryService;

    @Autowired
    public CategoryBlogController(CreateBlogCategoryService createBlogCategoryService) {
        this.createBlogCategoryService = createBlogCategoryService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/blog-categories")
    public ResponseEntity<?> createBlogCategory(@Valid @RequestBody CreateBlogCategoryRequestDTO blogCategoryRequestDTO) {

            CreateBlogCategoryResponseDTO blogCategoryResponseDTO = createBlogCategoryService.blogCategoryRequestDTO(blogCategoryRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(blogCategoryResponseDTO);

    }
}
