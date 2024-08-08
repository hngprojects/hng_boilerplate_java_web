package hng_java_boilerplate.comment_unit_test;

import hng_java_boilerplate.comment.entity.Comment;
import hng_java_boilerplate.comment.repository.CommentRepository;
import hng_java_boilerplate.comment.service.CommentService;
import hng_java_boilerplate.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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



}