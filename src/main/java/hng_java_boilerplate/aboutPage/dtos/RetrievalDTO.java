package hng_java_boilerplate.aboutPage.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RetrievalDTO {
    private String title;
    private String introduction;
    private CustomSectionDTO custom_sections;
    private int status_code;
    private String message;
}
