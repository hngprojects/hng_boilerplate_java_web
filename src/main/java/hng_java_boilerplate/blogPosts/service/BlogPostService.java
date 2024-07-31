package hng_java_boilerplate.blogPosts.service;

import hng_java_boilerplate.blogPosts.entity.BlogPost;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
@Service
public interface BlogPostService {

    Optional<BlogPost> getById(String blog_id);

    Collection<BlogPost> getAll();

    Optional<BlogPost> latestPost();

    BlogPost save(BlogPost post);

    BlogPost  updateById(String blog_id, BlogPost post);

    void delete(BlogPost post);

}