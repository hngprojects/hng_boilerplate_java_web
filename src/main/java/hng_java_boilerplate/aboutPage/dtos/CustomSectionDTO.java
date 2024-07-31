package hng_java_boilerplate.aboutPage.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomSectionDTO {
    private StatEntityDTO stats;
    private ServiceEntityDTO services;
}
