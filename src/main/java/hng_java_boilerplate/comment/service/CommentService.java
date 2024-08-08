package hng_java_boilerplate.comment.service;

import hng_java_boilerplate.comment.entity.Comment;
import hng_java_boilerplate.comment.repository.CommentRepository;
import hng_java_boilerplate.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}
