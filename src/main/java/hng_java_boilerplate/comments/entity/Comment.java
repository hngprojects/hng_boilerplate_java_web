package hng_java_boilerplate.comments.entity;

import hng_java_boilerplate.blogPosts.entity.BlogPost;
import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "comments")
public class Comment {
    private String text;
    private int likes = 0;
    private int dislikes = 0;
    @UuidGenerator
    @Column(name = "comment_id", nullable = false)
    private String commentId;
    @Column(columnDefinition = "Text", nullable = false)
    private String content;
    @JoinColumn(name = "parent_comment", nullable = false)
    private Comment parentComment;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private BlogPost post;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
   @Temporal(TemporalType.TIMESTAMP)
   @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Override
    public String toString() {
        return "Comment{" +
                "text='" + text + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", commentId='" + commentId + '\'' +
                ", content='" + content + '\'' +
                ", parentComment=" + parentComment +
                ", post=" + post +
                ", user=" + user +
                ", creationDate=" + creationDate +
                '}';
    }
}
