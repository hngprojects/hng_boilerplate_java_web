package hng_java_boilerplate.comment_unit_test;

import hng_java_boilerplate.blog.comment.entity.Comment;
import hng_java_boilerplate.blog.comment.repository.CommentRepository;
import hng_java_boilerplate.blog.comment.service.CommentService;
import hng_java_boilerplate.exception.exception_class.UnauthorizedException;
import hng_java_boilerplate.authentication.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createComment_ShouldReturnSavedComment() {
        // Arrange
        String userId = "user123";
        String name = "John Doe";
        String commentText = "This is my first comment.";

        Comment savedComment = new Comment();
        savedComment.setComment(commentText);
        User user = new User();
        user.setId(userId);
        user.setName(name);
        savedComment.setUser(user);
        savedComment.setCreatedAt(LocalDateTime.now());

        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        Comment result = commentService.createComment(userId, name, commentText);

        assertEquals(savedComment, result);
        assertEquals(commentText, result.getComment());
        assertEquals(userId, result.getUser().getId());
        assertEquals(name, result.getUser().getName());
        assertEquals(savedComment.getCreatedAt(), result.getCreatedAt());
    }


    @Test
    void isUserAuthorizedToDeleteComment_ShouldReturnTrueIfUserIsAuthorized() {
        String commentId = "comment1";
        String username = "user1";

        User user = new User();
        user.setId(username);

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setUser(user);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        boolean isAuthorized = commentService.isUserAuthorizedToDeleteComment(commentId, username);

        assertTrue(isAuthorized);
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void isUserAuthorizedToDeleteComment_ShouldThrowExceptionIfCommentNotFound() {
        String commentId = "comment1";
        String username = "user1";

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                commentService.isUserAuthorizedToDeleteComment(commentId, username));
    }

    @Test
    void softDeleteComment_ShouldSoftDeleteCommentIfUserIsAuthorized() {
        String commentId = "comment1";
        String userId = "user1";

        User user = new User();
        user.setId(userId);

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setUser(user);
        comment.setDeleted(false);

        when(commentRepository.findByCommentIdAndDeletedFalse(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.softDeleteComment(commentId, userId);

        assertTrue(result.getDeleted());  // Assuming getDeleted() is a method to check if the comment is soft deleted
        verify(commentRepository, times(1)).findByCommentIdAndDeletedFalse(commentId);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void softDeleteComment_ShouldThrowExceptionIfCommentNotFound() {
        String commentId = "comment1";
        String userId = "user1";

        when(commentRepository.findByCommentIdAndDeletedFalse(commentId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                commentService.softDeleteComment(commentId, userId));
    }

    @Test
    void softDeleteComment_ShouldThrowUnauthorizedExceptionIfUserIsNotAuthorized() {
        String commentId = "comment1";
        String userId = "user1";
        String differentUserId = "user2";

        User user = new User();
        user.setId(differentUserId);

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setUser(user);
        comment.setDeleted(false);

        when(commentRepository.findByCommentIdAndDeletedFalse(commentId)).thenReturn(Optional.of(comment));

        assertThrows(UnauthorizedException.class, () ->
                commentService.softDeleteComment(commentId, userId));
    }
}