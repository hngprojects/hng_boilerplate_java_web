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
public class BlogPostServiceImpl implements BlogPostService{
    private final BlogPostRepository blogPostRepository;

    @Override
    public Optional<BlogPost> getById(String blog_id) {
        return blogPostRepository.findPostById(blog_id);
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
    public BlogPost updateById(String blog_id, BlogPost post) {
        Optional<BlogPost> existingBlogPostOptional = blogPostRepository.findPostById(blog_id);
        if(existingBlogPostOptional.isPresent()){
            BlogPost existingPost = existingBlogPostOptional.get();
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            return blogPostRepository.saveAndFlush(post);
        }else{
            throw new EntityNotFoundException("The user with id" + blog_id + "not found");
        }
    }

    @Override
    public void delete(BlogPost post) {
        blogPostRepository.delete(post);

    }


}
