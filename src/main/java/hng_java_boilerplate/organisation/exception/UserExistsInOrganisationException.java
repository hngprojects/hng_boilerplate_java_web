package hng_java_boilerplate.organisation.exception;

public class UserExistsInOrganisationException extends RuntimeException {
    public UserExistsInOrganisationException(String message) {
        super(message);
    }
}
