package hng_java_boilerplate.organisation.exception;

public class PermissionNameAlreadyExistsException extends RuntimeException{
    public PermissionNameAlreadyExistsException() {
        super("Permission name already exists");
    }
}
