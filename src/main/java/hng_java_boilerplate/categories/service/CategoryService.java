package hng_java_boilerplate.categories.service;

import hng_java_boilerplate.categories.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(UUID id);
    void deleteCategory(UUID id);
}

