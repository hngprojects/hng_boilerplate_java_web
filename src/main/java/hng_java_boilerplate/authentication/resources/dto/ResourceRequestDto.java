package hng_java_boilerplate.authentication.resources.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRequestDto {

    private String id;
    private String title;
    private String description;
    private String image;
    private Boolean status;

}
