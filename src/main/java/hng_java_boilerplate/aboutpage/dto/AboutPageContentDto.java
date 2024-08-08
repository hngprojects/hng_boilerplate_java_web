package hng_java_boilerplate.aboutpage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AboutPageContentDto {
    @NotBlank
    private String title;

    @NotBlank
    private String introduction;

    @NotBlank
    private Map<String, Object> customSections;
}
