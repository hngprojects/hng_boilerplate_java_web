package hng_java_boilerplate.categories.controller;

import hng_java_boilerplate.categories.dto.CategoryDto;
import hng_java_boilerplate.categories.dto.CategoryRequest;
import hng_java_boilerplate.categories.dto.CustomResponse;
import hng_java_boilerplate.categories.entity.Category;
import hng_java_boilerplate.categories.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryRequest category) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(category));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
       return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> deleteCategory(@PathVariable UUID id) {
       return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
