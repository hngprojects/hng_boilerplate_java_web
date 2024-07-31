package hng_java_boilerplate;

import hng_java_boilerplate.blogPosts.entity.BlogPost;
import hng_java_boilerplate.blogPosts.repository.BlogPostRepository;
import hng_java_boilerplate.comments.entity.Comment;
import hng_java_boilerplate.comments.exceptions.BlogAPIException;
import hng_java_boilerplate.comments.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.comments.repository.CommentRepository;
import hng_java_boilerplate.comments.service.CommentServiceImpl;
import hng_java_boilerplate.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private BlogPost blogPost;
    private Comment comment;
    private User user;

    @BeforeEach
    void setUp() {
        blogPost = new BlogPost();
        blogPost.setId(UUID.randomUUID().toString());
        comment = new Comment();
        comment.setCommentId(UUID.randomUUID().toString());
        comment.setPost(blogPost);
        user = new User();
        user.setId(UUID.randomUUID().toString());
    }

    @Test
    void testCreateComment() {
        when(blogPostRepository.findPostById(blogPost.getId())).thenReturn(Optional.of(blogPost));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment createdComment = commentService.createComment(blogPost.getId(), comment);

        assertNotNull(createdComment);
        assertEquals(comment, createdComment);
        verify(blogPostRepository, times(1)).findPostById(blogPost.getId());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testCreateComment_PostNotFound() {
        when(blogPostRepository.findPostById(blogPost.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(blogPost.getId(), comment);
        });

        String expectedMessage = "Post with id" + blogPost.getId() + "not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(blogPostRepository, times(1)).findPostById(blogPost.getId());
    }

    @Test
    void testGetACommentForAPost() {
        when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.of(blogPost));
        when(commentRepository.findById(comment.getCommentId())).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.getACommentForAPost(blogPost.getId(), comment.getCommentId());

        assertNotNull(foundComment);
        assertEquals(comment, foundComment);
        verify(blogPostRepository, times(1)).findById(blogPost.getId());
        verify(commentRepository, times(1)).findById(comment.getCommentId());
    }

    @Test
    void testGetACommentForAPost_PostNotFound() {
        when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getACommentForAPost(blogPost.getId(), comment.getCommentId());
        });

        String expectedMessage = "Post not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(blogPostRepository, times(1)).findById(blogPost.getId());
    }

    @Test
    void testDeleteAComment() {
        when(blogPostRepository.findPostById(blogPost.getId())).thenReturn(Optional.of(blogPost));
        when(commentRepository.findById(comment.getCommentId())).thenReturn(Optional.of(comment));

        String result = commentService.deleteAComment(blogPost.getId(), comment.getCommentId());

        assertEquals("Comment with ID " + comment.getCommentId() + " belonging to post with ID " + blogPost.getId() + " deleted", result);
        verify(blogPostRepository, times(1)).findPostById(blogPost.getId());
        verify(commentRepository, times(1)).findById(comment.getCommentId());
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void testDeleteAComment_CommentNotBelongToPost() {
        BlogPost anotherPost = new BlogPost();
        anotherPost.setId(UUID.randomUUID().toString());
        comment.setPost(anotherPost);

        when(blogPostRepository.findPostById(blogPost.getId())).thenReturn(Optional.of(blogPost));
        when(commentRepository.findById(comment.getCommentId())).thenReturn(Optional.of(comment));

        Exception exception = assertThrows(BlogAPIException.class, () -> {
            commentService.deleteAComment(blogPost.getId(), comment.getCommentId());
        });

        String expectedMessage = "Comment does not belong to the post";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(blogPostRepository, times(1)).findPostById(blogPost.getId());
        verify(commentRepository, times(1)).findById(comment.getCommentId());
    }
}
