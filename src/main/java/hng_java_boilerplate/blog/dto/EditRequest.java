package hng_java_boilerplate.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditRequest {
    private String title;
    private String content;
    private List<String> tags;
}
