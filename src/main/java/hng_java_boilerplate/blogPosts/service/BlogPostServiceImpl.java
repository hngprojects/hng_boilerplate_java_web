package hng_java_boilerplate.blogPosts.service;

import hng_java_boilerplate.blogPosts.entity.BlogPost;
import hng_java_boilerplate.blogPosts.repository.BlogPostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostRepository blogPostRepository;

    @Override
    public Optional<BlogPost> getById(String blogId) {
        return blogPostRepository.findByBlogId(blogId)
                .or(() -> Optional.empty());
    }

    @Override
    public Collection<BlogPost> getAll() {
        return blogPostRepository.findAll();
    }

    @Override
    public Optional<BlogPost> latestPost() {
        return blogPostRepository.findTopByOrderByCreatedAtDesc();
    }

    @Override
    public BlogPost save(BlogPost post) {
        return blogPostRepository.saveAndFlush(post);
    }

    @Override
    public void delete(BlogPost post) {
        if (blogPostRepository.existsById(post.getBlogId())) {
            blogPostRepository.delete(post);
        } else {
            throw new EntityNotFoundException("Blog post not found");
        }
    }
}
