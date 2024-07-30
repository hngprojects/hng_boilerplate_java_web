package hng_java_boilerplate.organisation.exception;

public class OrganisationNameAlreadyExistsException extends RuntimeException {
    public OrganisationNameAlreadyExistsException(String msg){
        super(msg);
    }
}
