package hng_java_boilerplate.blogCategory.service;

import hng_java_boilerplate.blogCategory.dto.CategoryBlogData;
import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryRequestDTO;
import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryResponseDTO;
import hng_java_boilerplate.blogCategory.entity.Category;
import hng_java_boilerplate.blogCategory.exception.CategoryAlreadyExistsException;
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

    public CreateBlogCategoryResponseDTO blogCategoryRequestDTO(CreateBlogCategoryRequestDTO request) throws CategoryAlreadyExistsException {

        CreateBlogCategoryResponseDTO createBlogCategoryResponseDTO = new CreateBlogCategoryResponseDTO();

        Optional<Category> existingCategory = categoryRepository.findByName(request.getName());

        if ((existingCategory.isEmpty())) {

            Category category = new Category();
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
            throw new CategoryAlreadyExistsException("A category with the same name already exists.");
        }


        }

}