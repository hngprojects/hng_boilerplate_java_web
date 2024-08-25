package hng_java_boilerplate.blogPost.service;

import hng_java_boilerplate.blogPost.controller.dto.BlogPostDTO;
import hng_java_boilerplate.blogPost.entity.BlogPost;
import hng_java_boilerplate.blogPost.repository.BlogPostRepository;
import hng_java_boilerplate.exception.UnAuthorizedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostRepository blogPostRepository;

    @Override
    public BlogPostDTO save(BlogPostDTO post) {

        BlogPost savedPost = new BlogPost();
        BlogPostDTO saveDTO = new BlogPostDTO();
        saveDTO.setTitle(savedPost.getTitle());
        saveDTO.setContent(savedPost.getContent());
        saveDTO.setImageUrls(savedPost.getImageUrls());
        saveDTO.setTags(savedPost.getTags());

        return saveDTO;
    }

    @Override
    public void delete(BlogPost blogId) {
        public ResponseEntity<String> deleteBlogPostById(@PathVariable("blog_id") String blogId) {
            try {
                blogPostService.deleteById(blogId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Blog post deleted successfully");
            } catch (BlogPostNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog post not found");
            } catch (UnAuthorizedException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this blog post");
            }
        }

    }}
