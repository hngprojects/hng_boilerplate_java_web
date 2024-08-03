package hng_java_boilerplate.blogPost.service_unit_test;

import hng_java_boilerplate.blogPosts.entity.BlogPost;
import hng_java_boilerplate.blogPosts.repository.BlogPostRepository;
import hng_java_boilerplate.blogPosts.service.BlogPostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.HtmlUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogPostServiceImplTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private BlogPostServiceImpl blogPostService;

    private BlogPost blogPost;

    @BeforeEach
    void setUp() {
        blogPost = new BlogPost();
        blogPost.setId(UUID.randomUUID().toString());
        blogPost.setTitle("Test Title");
        blogPost.setContent("Test Content");
    }

    @Test
    void testGetById() {
        when(blogPostRepository.findPostById(blogPost.getId())).thenReturn(Optional.of(blogPost));

        Optional<BlogPost> foundBlogPost = blogPostService.getById(blogPost.getId());

        assertTrue(foundBlogPost.isPresent());
        assertEquals(blogPost, foundBlogPost.get());
        verify(blogPostRepository, times(1)).findPostById(blogPost.getId());
    }

    @Test
    void testGetById_NotFound() {
        when(blogPostRepository.findPostById(blogPost.getId())).thenReturn(Optional.empty());

        Optional<BlogPost> foundBlogPost = blogPostService.getById(blogPost.getId());

        assertFalse(foundBlogPost.isPresent());
        verify(blogPostRepository, times(1)).findPostById(blogPost.getId());
    }

    @Test
    void testGetAll() {
        BlogPost anotherPost = new BlogPost();
        anotherPost.setId(UUID.randomUUID().toString());
        anotherPost.setTitle("Another Title");
        anotherPost.setContent("Another Content");

        when(blogPostRepository.findAll()).thenReturn(List.of(blogPost, anotherPost));

        Collection<BlogPost> blogPosts = blogPostService.getAll();

        assertEquals(2, blogPosts.size());
        verify(blogPostRepository, times(1)).findAll();
    }

    @Test
    void testLatestPost() {
        when(blogPostRepository.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.of(blogPost));

        Optional<BlogPost> latestBlogPost = blogPostService.latestPost();

        assertTrue(latestBlogPost.isPresent());
        assertEquals(blogPost, latestBlogPost.get());
        verify(blogPostRepository, times(1)).findTopByOrderByCreatedAtDesc();
    }

    @Test
    void testSave() {

        String sanitizeTitle = HtmlUtils.htmlEscape(blogPost.getTitle());
        String sanitizeContent = HtmlUtils.htmlEscape(blogPost.getContent());

        blogPost.setTitle(sanitizeTitle);
        blogPost.setContent(sanitizeContent);

        when(blogPostRepository.saveAndFlush(blogPost)).thenReturn(blogPost);

        BlogPost savedBlogPost = blogPostService.save(blogPost);

        assertNotNull(savedBlogPost);
        assertEquals(sanitizeTitle, savedBlogPost.getTitle());
        assertEquals(sanitizeContent, savedBlogPost.getContent());
        verify(blogPostRepository, times(1)).saveAndFlush(blogPost);
    }

    @Test
    void testDelete() {
        doNothing().when(blogPostRepository).delete(blogPost);

        blogPostService.delete(blogPost);

        verify(blogPostRepository, times(1)).delete(blogPost);
    }
}
