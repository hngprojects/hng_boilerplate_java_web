package hng_java_boilerplate.organisation.dto;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.InvitationValidationException;
import hng_java_boilerplate.organisation.exception.response.ErrorResponse;
import hng_java_boilerplate.organisation.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;

public class InvitationRequest {
    @Autowired
    private OrganisationService organisationService;

    private String ordId;
    private String expires;

    public String getOrdId() {
        return ordId;
    }
    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

}
