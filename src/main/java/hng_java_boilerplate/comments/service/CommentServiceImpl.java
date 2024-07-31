package hng_java_boilerplate.comments.service;

import hng_java_boilerplate.blogPosts.entity.BlogPost;
import hng_java_boilerplate.blogPosts.repository.BlogPostRepository;
import hng_java_boilerplate.comments.entity.Comment;
import hng_java_boilerplate.comments.exceptions.BlogAPIException;
import hng_java_boilerplate.comments.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.comments.exceptions.UnAuthorizeException;
import hng_java_boilerplate.comments.repository.CommentRepository;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;


    @Override
    public Comment createComment(String blog_id, Comment comment) {
        BlogPost post = blogPostRepository.findPostById(blog_id)
        .orElseThrow(()-> new ResourceNotFoundException("Post with id" + blog_id + "not found"));
        comment.setPost(post);
        return commentRepository.save(comment);

    }

    @Override
    public Collection<org.hibernate.annotations.Comment> getCommentsForAPost(String blog_id) {
        BlogPost post = blogPostRepository.findPostById(blog_id)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found"));

        return post.getComments();
    }

    @Override
    public Comment getACommentForAPost(String blog_id, String commentId) {
        BlogPost post = blogPostRepository.findById(blog_id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id" + commentId+"not found"));
        if(comment.getPost().getId().equals(post.getId())){
           return comment;
        }else{ throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }

    }
    @Override
    public String deleteAComment(String commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found"));
        commentRepository.delete(comment);
        return "Comment Successfully deleted";
    }

    @Override
    @Transactional
    public Comment replyToComment(String commentId, Comment reply, GetUserDto userDto) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment with id" + commentId+"not found"));
        User user = userRepository.findById(userDto.getId()).orElseThrow(()-> new UnAuthorizeException("User not found"));
        reply.setParentComment(parentComment);
        reply.setUser(user);
        return commentRepository.save(reply);
    }

    @Override
    @Transactional
    public Comment likeComment(String commentId, GetUserDto user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment not found"));
        comment.setLikes(comment.getLikes() + 1);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment dislikeComment(String commentId, GetUserDto user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("comment not found"));
        comment.setDislikes(comment.getDislikes() +1);
        return commentRepository.save(comment);
    }

    @Override
    public Comment editComment(String commentId, String newText, GetUserDto user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new UnAuthorizeException("User has no permission to edit comment"));
        comment.setText("newText");
        return commentRepository.save(comment);
    }
}


