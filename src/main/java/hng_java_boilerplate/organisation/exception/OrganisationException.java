package hng_java_boilerplate.organisation.exception;

import java.util.function.Supplier;

public class OrganisationException extends RuntimeException{
    public OrganisationException(String message) {
        super(message);
    }
}
