package hng_java_boilerplate.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditResponse {
    private String id;
    private String title;
    private String content;
    private List<String> tags;
    private String updated_at;
}
