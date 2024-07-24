package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.dto.InvitationRequest;
import hng_java_boilerplate.organisation.dto.ValidLinkResponse;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.InvitationValidationException;
import hng_java_boilerplate.organisation.exception.OrganisationException;
import hng_java_boilerplate.organisation.exception.response.ErrorResponse;
import hng_java_boilerplate.organisation.exception.response.SuccessResponse;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.dto.GetUserDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.crypto.BadPaddingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    private final UserRepository userRepository;


    private final UserService userService;

    public Organisation getOrganisationDetails(String organisationId) throws OrganisationException{
        Organisation foundOrganisation = organisationRepository.findById(organisationId)
                .orElseThrow(()-> new OrganisationException("Invalid Organisation Id"));
        return foundOrganisation;
    }

    public ResponseEntity<?> validateAndAcceptUserToOrganisation(String invitationLink) throws InvitationValidationException {
        //        This is a secured method and while i await Auth guy to come through so i can fetch the id of logged-in user
// TODO: 7/20/2024 fetch user that wants to be added to organisation from the logged in user
        //        A demo user

       ValidLinkResponse validInviteLink= validateInviteLink(invitationLink);
        String userId = validInviteLink.getUserId();
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new OrganisationException("Invalid user id"));
        Organisation organisationDetails = getOrganisationDetails(validInviteLink.getOrganisationId());
        List<Organisation> organisations = foundUser.getOrganisations();
        if (foundUser.getOrganisations().contains(organisationDetails)){
            throw new OrganisationException("User already belong to organisation");
        }
        organisations.add(organisationDetails);
        foundUser.setOrganisations(organisations);
        userRepository.save(foundUser);
        SuccessResponse response = new SuccessResponse(
                "Invitation accepted, you have been added to the organization",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }


    private void validateField(InvitationRequest invitationRequest ){
        ErrorResponse errorResponse = new ErrorResponse();
        List<String> error = new ArrayList<>();
        Organisation organisationDetails = getOrganisationDetails(invitationRequest.getOrganisationId());
        ZonedDateTime expirationTime = ZonedDateTime.parse(invitationRequest.getExpires());
        if (invitationRequest.getOrganisationId()== null|| invitationRequest.getExpires() == null){
            error.add("Invalid link format");
        }
        if (organisationDetails == null){
            error.add("Organization not found");
        }
        if (ZonedDateTime.now().isAfter(expirationTime)){
            error.add("Expired Invitation Link");
        }
        errorResponse.setError(error);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        if (!error.isEmpty()){
            errorResponse.setMessage("Invalid or expired invitation Link");
            throw new InvitationValidationException(
                    errorResponse.getMessage(),
                    errorResponse.getError(),
                    errorResponse.getStatus()
                    );
        }

    }

    public ValidLinkResponse validateInviteLink(String invitationLink) throws InvitationValidationException{
        ErrorResponse errorResponse = new ErrorResponse();
        List<String> error = new ArrayList<>();
        String invitationLink1 = invitationLink;
        Pattern pattern = Pattern.compile("organisationId=([\\w-]+)&userId=([\\w-]+)&expires=([\\d]{4}-[\\d]{2}-[\\d]{2}T[\\d]{2}:[\\d]{2}:[\\d]{2}Z)");
        Matcher matcher = pattern.matcher(invitationLink1);
        String organisationId=null;
        String userId = null;
        String expires= null;
        if (matcher.find()) {
             organisationId = matcher.group(1);
             userId = matcher.group(2);
             expires = matcher.group(3);
        } else {
            error.add("Invalid link format not recognized");
            errorResponse.setMessage("Invalid or expired invitation Link");
            errorResponse.setError(error);
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new InvitationValidationException(
                    errorResponse.getMessage(),
                    errorResponse.getError(),
                    errorResponse.getStatus()
            );
        }
        System.out.println("organisationId: " + organisationId);
        System.out.println("expireTime: " + expires);
        ValidLinkResponse validLinkResponse = new ValidLinkResponse(
                organisationId,
                userId
        );
        ZonedDateTime expirationTime= null;
        try{
            if (expires == null || expires.isEmpty()) {
                throw new OrganisationException("Expiration time is missing.");
            }
            expirationTime = ZonedDateTime.parse(expires);
        }catch (DateTimeParseException e){
            throw new OrganisationException("Invalid time format");
        }

        Organisation organisationDetails = getOrganisationDetails(organisationId);
        if (organisationId == null || expires == null){
            error.add("Invalid link format");
        }
        if (organisationDetails == null){
            error.add("Organization not found");
        }
        if (ZonedDateTime.now().isAfter(expirationTime)){
            error.add("Expired Invitation Link");
        }
        errorResponse.setError(error);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        if (!error.isEmpty()){
            errorResponse.setMessage("Invalid or expired invitation Link");
            throw new InvitationValidationException(
                    errorResponse.getMessage(),
                    errorResponse.getError(),
                    errorResponse.getStatus()
            );
        }
        return validLinkResponse;
    }




}
