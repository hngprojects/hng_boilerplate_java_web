package hng_java_boilerplate.externalPage.aboutpage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @JsonProperty("custom_sections")
    private Map<String, Object> customSections;
}
