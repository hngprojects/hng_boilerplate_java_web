package hng_java_boilerplate.externalPages.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ServiceEntityDTO {
    private String title;
    private String description;
}
