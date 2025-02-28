package hng_java_boilerplate.organisation.exception;

public class OrganisationNotFoundException extends RuntimeException {
    public OrganisationNotFoundException(String message) {
        super(message);
    }
}
