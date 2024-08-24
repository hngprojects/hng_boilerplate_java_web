package hng_java_boilerplate.blogpostTest;
import hng_java_boilerplate.blogPost.controller.dto.BlogPostDTO;
import hng_java_boilerplate.blogPost.entity.BlogPost;
import hng_java_boilerplate.blogPost.repository.BlogPostRepository;
import hng_java_boilerplate.blogPost.service.BlogPostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class create_comment_test {


    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private BlogPostServiceImpl blogPostService;

    private BlogPostDTO blogPostDTO;
    private BlogPost blogPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        blogPostDTO = new BlogPostDTO();
        blogPostDTO.setTitle("Test Title");
        blogPostDTO.setContent("Test Content");
        blogPostDTO.setImageUrls(Arrays.asList("http://image.url"));
        blogPostDTO.setTags(Arrays.asList("Tag1", "Tag2"));

        blogPost = new BlogPost();
        // Initialize blogPost fields from blogPostDTO here
        blogPost.setTitle(blogPostDTO.getTitle());
        blogPost.setContent(blogPostDTO.getContent());
        blogPost.setImageUrls(blogPostDTO.getImageUrls());
        blogPost.setTags(blogPostDTO.getTags());
        // Set a unique ID or other fields as needed
    }

    @Test
    void testSave() {
        when(blogPostRepository.saveAndFlush(any(BlogPost.class))).thenReturn(blogPost);

        BlogPostDTO savedPost = blogPostService.save(blogPostDTO);

        assertNotNull(savedPost);
        assertEquals(blogPostDTO.getTitle(), savedPost.getTitle());
        assertEquals(blogPostDTO.getContent(), savedPost.getContent());
    }
}
