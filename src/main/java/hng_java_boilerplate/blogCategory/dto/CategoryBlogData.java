package hng_java_boilerplate.blogCategory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hng_java_boilerplate.blogCategory.entity.BlogCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryBlogData {

    private BlogCategory name;

}
