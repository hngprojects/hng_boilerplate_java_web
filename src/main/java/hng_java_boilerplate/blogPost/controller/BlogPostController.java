package hng_java_boilerplate.blogPost.controller;

import hng_java_boilerplate.blogPost.controller.dto.BlogPostDTO;
import hng_java_boilerplate.blogPost.entity.BlogPost;
import hng_java_boilerplate.blogPost.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/blogpost")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN')")
    @PostMapping("/createBlogPost")
    public ResponseEntity<BlogPostDTO> createBlogPost(@RequestBody BlogPostDTO blogPostDTO) {
        BlogPostDTO savePost = blogPostService.save(blogPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savePost);
    }
}
