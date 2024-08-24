package hng_java_boilerplate.blogPost.controller.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostDTO {
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

}
