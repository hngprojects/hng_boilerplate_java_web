package hng_java_boilerplate.blogCategory.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateBlogCategoryResponseDTO {

    private String status;
    private String message;
    private CategoryBlogData data;
    private String status_Code;

    public CreateBlogCategoryResponseDTO(String status, String message, CategoryBlogData data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public CreateBlogCategoryResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CategoryBlogData getData() {
        return data;
    }

    public void setData(CategoryBlogData data) {
        this.data = data;
    }
}
