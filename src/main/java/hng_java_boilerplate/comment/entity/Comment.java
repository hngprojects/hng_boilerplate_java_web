package hng_java_boilerplate.comment.entity;


import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comments")

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String commentId;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private Boolean deleted = false;

    @NotBlank(message = "input comment")
    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;



}
