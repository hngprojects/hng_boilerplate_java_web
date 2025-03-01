package hng_java_boilerplate.comment.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hng_java_boilerplate.comment.entity.Comment;
import hng_java_boilerplate.comment.repository.CommentRepository;
import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public Comment createComment(String userId, String name,  String comment){
        User user = new User();
        user.setId(userId);
        user.setName(name);
        Comment newComment = new Comment();
        newComment.setComment(comment);
        newComment.setUser(user);
        newComment.setCreatedAt(LocalDateTime.now());

     return commentRepository.save(newComment);
    }

    public Boolean isUserAuthorizedToDeleteComment(String commentId, String username){
        Comment comment = commentRepository.findById(commentId)
        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment not found"));

        return comment.getUser().getId().equals(username);

    }

    public Boolean isUserAuthorizedToUpdateComment(String commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment not found"));
        return null;
    }
    
    public Comment softDeleteComment (String commentId, String userId){
        Comment comment = commentRepository.findByCommentIdAndDeletedFalse(commentId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnAuthorizedException("Unauthorized user");
        }
        comment.setDeleted(true);
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
        }

// The service that handles the logic for updating a comment
// The method takes in the commentId, userId, and the new comment text
// It returns the updated comment
    public Comment updateComment(String commentId, String userId, String newCommentText) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));


        
        // Ensure the user exists
        userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    
        // Authorization check
        if (!comment.getUser().getId().equals(userId)) {
            throw new UnAuthorizedException("Unable to update comment");
        } 
    
        // Update the comment text
        comment.setComment(newCommentText);
        return commentRepository.save(comment);
    }    

}
