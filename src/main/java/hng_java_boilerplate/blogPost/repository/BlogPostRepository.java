package hng_java_boilerplate.blogPost.repository;

import hng_java_boilerplate.blogPost.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, String> {

    Optional<BlogPost> findByBlogId(String blogId);


    Optional<BlogPost> findTopByOrderByCreatedAtDesc();

}