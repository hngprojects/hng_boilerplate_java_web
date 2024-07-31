package hng_java_boilerplate.comments.repository;

import hng_java_boilerplate.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
