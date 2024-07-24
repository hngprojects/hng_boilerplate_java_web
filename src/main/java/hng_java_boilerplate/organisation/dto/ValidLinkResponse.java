package hng_java_boilerplate.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidLinkResponse {
    private String organisationId;
    private String userId;
}
