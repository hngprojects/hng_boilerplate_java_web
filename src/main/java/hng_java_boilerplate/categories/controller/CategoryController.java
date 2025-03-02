package hng_java_boilerplate.categories.controller;

import hng_java_boilerplate.categories.dto.CategoryDto;
import hng_java_boilerplate.categories.dto.CategoryRequest;
import hng_java_boilerplate.categories.dto.CustomResponse;
import hng_java_boilerplate.categories.service.CategoryService;
import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Category", description = "Category Controller")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "create category success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    @Operation(summary = "User creates Category")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryRequest category) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(category));
    }


    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "retrieve all category success",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @Operation(summary = "User create Category")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
       return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get by category id success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Category not found by ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @Operation(summary = "User retrieves category by it's ID")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }


    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "delete category by ID success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @Operation(summary = "Delete a category by id")
    public ResponseEntity<CustomResponse> deleteCategory(@PathVariable UUID id) {
       return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
