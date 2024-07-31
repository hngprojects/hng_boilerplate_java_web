package hng_java_boilerplate.blogPosts.entity;

import hng_java_boilerplate.util.UUIDGenarator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPost {

    private String id;
   @UuidGenerator
    @Column(name = "blog_id", nullable = false)
    private String blog_id;
    @NotEmpty(message = "Enter your title")
    @Column(name = "title", nullable = false)
    private String title;
    @NotEmpty(message = "Write your content")
    @Column(name = "content", nullable = false, columnDefinition = "Text")
      private String content;
    @NotEmpty(message = "Add image link")
    @Column(name = "image_url", nullable = false)
    private List<String> imageUrls;
    @Column(name = "tag", nullable = true)
    private List<String> tags;
@NotNull
@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private BlogSuperAdminUser author;
   @CreationTimestamp
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime createdAt;

@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
   private Collection<Comment> comments;
}
