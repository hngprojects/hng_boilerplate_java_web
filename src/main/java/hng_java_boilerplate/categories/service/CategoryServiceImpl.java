package hng_java_boilerplate.categories.service;

import hng_java_boilerplate.categories.dto.CategoryDto;
import hng_java_boilerplate.categories.dto.CategoryRequest;
import hng_java_boilerplate.categories.dto.CustomResponse;
import hng_java_boilerplate.categories.entity.Category;
import hng_java_boilerplate.categories.repository.CategoryRepository;
import hng_java_boilerplate.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryRequest request) {
        Category newCategory = new Category();
        newCategory.setName(request.getName());
        newCategory.setDescription(request.getDescription());
        newCategory.setSlug(request.getSlug());
        newCategory.setCreatedAt(LocalDateTime.now());
        newCategory.setUpdatedAt(LocalDateTime.now());

        categoryRepository.saveAndFlush(newCategory);

        return mapCategory(newCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories =  categoryRepository.findAll();

        return  categories.stream()
                .map(this::mapCategory)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("category not found with id"));

        return mapCategory(category);
    }

    @Override
    public CustomResponse deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("category not found with id" + id));
        categoryRepository.delete(category);

        return new CustomResponse(200, "category deleted successfully");
    }

    private CategoryDto mapCategory(Category category) {
        return CategoryDto.builder()
                .category_id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .slug(category.getSlug())
                .created_at(category.getCreatedAt())
                .updated_at(category.getUpdatedAt())
                .build();
    }
}

