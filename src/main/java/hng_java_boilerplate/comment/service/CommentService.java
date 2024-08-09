package hng_java_boilerplate.comment.service;


import hng_java_boilerplate.comment.entity.Comment;
import hng_java_boilerplate.comment.repository.CommentRepository;
import hng_java_boilerplate.profile.exceptions.UnauthorizedException;
import hng_java_boilerplate.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

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
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment not found"));

        return comment.getUser().getId().equals(username);

    }

    public Comment softDeleteComment(String commentId, String userId){
        Comment comment = commentRepository.findByCommentIdAndDeletedFalse(commentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if(!comment.getUser().getId().equals(userId)){
            throw new UnauthorizedException("Unauthorized user");
        }
        comment.setDeleted(true);
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }


}
