package hng_java_boilerplate.organisation.dto;
import lombok.Data;
@Data
public class InvitationRequest {
    private String organisationId;
    private String expires;
}
