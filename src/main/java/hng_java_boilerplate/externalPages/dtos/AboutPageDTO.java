package hng_java_boilerplate.externalPages.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AboutPageDTO {
    private Long id;
    private String title;
    private String introduction;
    private CustomSectionDTO custom_sections;
}
