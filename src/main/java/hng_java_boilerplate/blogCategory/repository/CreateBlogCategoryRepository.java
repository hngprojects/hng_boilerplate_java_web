package hng_java_boilerplate.blogCategory.repository;

import hng_java_boilerplate.blogCategory.entity.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CreateBlogCategoryRepository extends JpaRepository<BlogCategory, String> {

    Optional<BlogCategory> findByName(String name);

}
