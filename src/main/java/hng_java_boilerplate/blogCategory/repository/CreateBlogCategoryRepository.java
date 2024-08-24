package hng_java_boilerplate.blogCategory.repository;

import hng_java_boilerplate.blogCategory.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CreateBlogCategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByName(String name);

}
