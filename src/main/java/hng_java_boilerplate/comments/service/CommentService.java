package hng_java_boilerplate.comments.service;

import hng_java_boilerplate.comments.entity.Comment;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public interface CommentService {

    Comment createComment(String blog_id, Comment comment);

    Collection<org.hibernate.annotations.Comment> getCommentsForAPost(String blog_id);

    Comment getACommentForAPost(String blog_id, String commentId);

    String deleteAComment(String commentId);

    @Transactional
    Comment replyToComment(String commentId, Comment reply, GetUserDto user);

    @Transactional
    Comment likeComment(String commentId, GetUserDto user);

    @Transactional
    Comment dislikeComment(String commentId, GetUserDto user);

    @Transactional
    Comment editComment(String commentId, String newText, GetUserDto user);

}



