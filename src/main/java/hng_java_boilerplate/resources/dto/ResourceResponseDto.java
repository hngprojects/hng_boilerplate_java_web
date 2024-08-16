package hng_java_boilerplate.resources.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hng_java_boilerplate.resources.entity.Resources;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ResourceResponseDto {

    private String message;
    private List<Resources> data;
    private Resources resourceData;
    private Integer totalPages;
    private Integer currentPage;

}
