package hng_java_boilerplate.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationDetail {
    private String orgId;
    private String name;
    private String description;
}
