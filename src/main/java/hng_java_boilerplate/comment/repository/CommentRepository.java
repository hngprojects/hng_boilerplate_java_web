package hng_java_boilerplate.comment.repository;


import hng_java_boilerplate.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    Optional<Comment> findByCommentIdAndDeletedFalse(String commentId);

}
