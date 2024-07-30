package hng_java_boilerplate.externalPages.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomSectionDTO {
    private StatEntityDTO stats;
    private ServiceEntityDTO services;
}
