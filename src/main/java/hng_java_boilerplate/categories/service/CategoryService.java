package hng_java_boilerplate.categories.service;

import hng_java_boilerplate.categories.dto.CategoryDto;
import hng_java_boilerplate.categories.dto.CategoryRequest;
import hng_java_boilerplate.categories.dto.CustomResponse;
import hng_java_boilerplate.categories.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(CategoryRequest category);
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(UUID category_id);
    CustomResponse deleteCategory(UUID category_id);
}

