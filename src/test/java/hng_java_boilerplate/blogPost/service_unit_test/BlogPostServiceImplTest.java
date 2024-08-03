package hng_java_boilerplate.blogPost.service_unit_test;

import hng_java_boilerplate.blogPosts.entity.BlogPost;
import hng_java_boilerplate.blogPosts.repository.BlogPostRepository;
import hng_java_boilerplate.blogPosts.service.BlogPostServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlogPostServiceImplTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private BlogPostServiceImpl blogPostService;

    private BlogPost blogPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        blogPost = new BlogPost();
        blogPost.setBlogId("1");
        blogPost.setTitle("Test Title");
        blogPost.setContent("Test Content");
        blogPost.setImageUrls(Arrays.asList("http://image.url"));
        blogPost.setTags(Arrays.asList("Tag1", "Tag2"));
    }

    @Test
    void testGetById() {
        when(blogPostRepository.findByBlogId(blogPost.getBlogId())).thenReturn(Optional.of(blogPost));

        Optional<BlogPost> foundPost = blogPostService.getById(blogPost.getBlogId());

        assertTrue(foundPost.isPresent());
        assertEquals(blogPost.getBlogId(), foundPost.get().getBlogId());
        verify(blogPostRepository, times(1)).findByBlogId(blogPost.getBlogId());
    }

    @Test
    void testGetAll() {
        List<BlogPost> blogPosts = Arrays.asList(blogPost);
        when(blogPostRepository.findAll()).thenReturn(blogPosts);

        Collection<BlogPost> foundPosts = blogPostService.getAll();

        assertEquals(1, foundPosts.size());
        verify(blogPostRepository, times(1)).findAll();
    }

    @Test
    void testLatestPost() {
        when(blogPostRepository.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.of(blogPost));

        Optional<BlogPost> foundPost = blogPostService.latestPost();

        assertTrue(foundPost.isPresent());
        assertEquals(blogPost.getBlogId(), foundPost.get().getBlogId());
        verify(blogPostRepository, times(1)).findTopByOrderByCreatedAtDesc();
    }

    @Test
    void testSave() {
        when(blogPostRepository.saveAndFlush(blogPost)).thenReturn(blogPost);

        BlogPost savedPost = blogPostService.save(blogPost);

        assertNotNull(savedPost);
        assertEquals(blogPost.getBlogId(), savedPost.getBlogId());
        verify(blogPostRepository, times(1)).saveAndFlush(blogPost);
    }

    @Test
    void testDelete() {
        when(blogPostRepository.existsById(blogPost.getBlogId())).thenReturn(true);

        blogPostService.delete(blogPost);

        verify(blogPostRepository, times(1)).delete(blogPost);
    }
    @Test
    void testDeleteThrowsExceptionWhenNotFound() {
        when(blogPostRepository.existsById(blogPost.getBlogId())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> blogPostService.delete(blogPost));

        verify(blogPostRepository, never()).delete(blogPost);
    }
}
