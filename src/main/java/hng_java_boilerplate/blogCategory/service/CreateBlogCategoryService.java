package hng_java_boilerplate.blogCategory.service;

import hng_java_boilerplate.blogCategory.dto.BlogCategoryRequestDTO;
import hng_java_boilerplate.blogCategory.dto.BlogCategoryResponseDto;
import hng_java_boilerplate.blogCategory.dto.CategoryBlogData;
import hng_java_boilerplate.blogCategory.entity.BlogCategory;
import hng_java_boilerplate.blogCategory.exception.BlogCategoryAlreadyExistsException;
import hng_java_boilerplate.blogCategory.repository.CreateBlogCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateBlogCategoryService {
    private final CreateBlogCategoryRepository categoryRepository;
    @Autowired
    public CreateBlogCategoryService(CreateBlogCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public BlogCategoryResponseDto CreateBlogCategoryRequestDTO(BlogCategoryRequestDTO request) throws BlogCategoryAlreadyExistsException {

        BlogCategoryResponseDto createBlogCategoryResponseDTO = new BlogCategoryResponseDto();
        Optional<BlogCategory> existingCategory = categoryRepository.findByName(request.getName());

        if ((existingCategory.isEmpty())) {
            BlogCategory category = new BlogCategory();
            category.setName(request.getName());
            categoryRepository.save(category);

            createBlogCategoryResponseDTO.setStatus("success");
            createBlogCategoryResponseDTO.setMessage("Blog category created successfully.");

            CategoryBlogData categoryBlogData = new CategoryBlogData();
            categoryBlogData.setName(category);
            createBlogCategoryResponseDTO.setData(categoryBlogData);
            return createBlogCategoryResponseDTO;
        }
        else {
            throw new BlogCategoryAlreadyExistsException("A category with the same name already exists.");
        }
    }
}


