package hng_java_boilerplate;
import hng_java_boilerplate.comments.exceptions.UnAuthorizeException;
import hng_java_boilerplate.comments.service.CommentServiceImpl;
import hng_java_boilerplate.blogPosts.entity.BlogPost;
import hng_java_boilerplate.blogPosts.repository.BlogPostRepository;
import hng_java_boilerplate.comments.entity.Comment;
import hng_java_boilerplate.comments.exceptions.BlogAPIException;
import hng_java_boilerplate.comments.repository.CommentRepository;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment() {
        String blog_id = "1";
        Comment comment = new Comment();
        BlogPost blogPost = new BlogPost();
        blogPost.setId(blog_id);

        when(blogPostRepository.findPostById(blog_id)).thenReturn(Optional.of(blogPost));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.createComment(blog_id, comment);

        assertEquals(blogPost, comment.getPost());
        assertEquals(comment, result);
        verify(blogPostRepository).findPostById(blog_id);
        verify(commentRepository).save(comment);
    }

    @Test
    void testGetCommentsForAPost() {
        String blog_id = "1";
        BlogPost blogPost = new BlogPost();

        when(blogPostRepository.findPostById(blog_id)).thenReturn(Optional.of(blogPost));

        var result = commentService.getCommentsForAPost(blog_id);

        assertNotNull(result);
        verify(blogPostRepository).findPostById(blog_id);
    }

    @Test
    void testGetACommentForAPost() {
        String blog_id = "1";
        String commentId = "2";
        BlogPost blogPost = new BlogPost();
        blogPost.setId(blog_id);
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setPost(blogPost);

        when(blogPostRepository.findById(blog_id)).thenReturn(Optional.of(blogPost));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Comment result = commentService.getACommentForAPost(blog_id, commentId);

        assertEquals(comment, result);
        verify(blogPostRepository).findById(blog_id);
        verify(commentRepository).findById(commentId);
    }

    @Test
    void testGetACommentForAPost_CommentNotBelongToPost() {
        String blog_id = "1";
        String commentId = "2";
        BlogPost blogPost = new BlogPost();
        blogPost.setId(blog_id);
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setPost(new BlogPost());

        when(blogPostRepository.findById(blog_id)).thenReturn(Optional.of(blogPost));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(BlogAPIException.class, () -> commentService.getACommentForAPost(blog_id, commentId));
    }

    @Test
    void testDeleteAComment() {
        String commentId = "1";
        Comment comment = new Comment();
        comment.setCommentId(commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        String result = commentService.deleteAComment(commentId);

        assertEquals("Comment Successfully deleted", result);
        verify(commentRepository).findById(commentId);
        verify(commentRepository).delete(comment);
    }

    @Test
    void testReplyToComment() {
        String commentId = "1";
        Comment parentComment = new Comment();
        parentComment.setCommentId(commentId);
        Comment reply = new Comment();
        User user = new User();
        GetUserDto userDto = new GetUserDto();
        userDto.setId("user1");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(parentComment));
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(commentRepository.save(reply)).thenReturn(reply);

        Comment result = commentService.replyToComment(commentId, reply, userDto);

        assertEquals(parentComment, reply.getParentComment());
        assertEquals(user, reply.getUser());
        verify(commentRepository).findById(commentId);
        verify(userRepository).findById(userDto.getId());
        verify(commentRepository).save(reply);
    }

    @Test
    void testLikeComment() {
        String commentId = "1";
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setLikes(0);
        GetUserDto userDto = new GetUserDto();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.likeComment(commentId, userDto);

        assertEquals(1, comment.getLikes());
        assertEquals(comment, result);
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(comment);
    }

    @Test
    void testDislikeComment() {
        String commentId = "1";
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setDislikes(0);
        GetUserDto userDto = new GetUserDto();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.dislikeComment(commentId, userDto);

        assertEquals(1, comment.getDislikes());
        assertEquals(comment, result);
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(comment);
    }

    @Test
    void testEditComment() {
        String commentId = "1";
        String newText = "newText";
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        GetUserDto userDto = new GetUserDto();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.editComment(commentId, newText, userDto);

        assertEquals(newText, comment.getText());
        assertEquals(comment, result);
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(comment);
    }
}

