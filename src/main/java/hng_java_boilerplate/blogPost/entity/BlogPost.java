package hng_java_boilerplate.blogPost.entity;

import hng_java_boilerplate.comment.entity.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BlogPost {

    @Id
    @UuidGenerator
    @Column(name = "blog_id", nullable = false)
    private String blogId;
    @NotEmpty(message = "Enter your title")
    @Column(name = "title", nullable = false)
    private String title;
    @NotEmpty(message = "Write your content")
    @Column(name = "content", nullable = false, columnDefinition = "Text")
    private String content;
    @NotEmpty(message = "Add image link")
    @Column(name = "image_url", nullable = false)
    private List<String> imageUrls;
    @Column(name = "tag")
    private List<String> tags;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany
    @JoinColumn()
    private List<Comment> comment;
}
