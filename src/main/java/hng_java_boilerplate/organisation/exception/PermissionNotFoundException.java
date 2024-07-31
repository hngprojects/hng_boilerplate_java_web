package hng_java_boilerplate.organisation.exception;

public class PermissionNotFoundException extends RuntimeException{
    public PermissionNotFoundException() {
        super("Permission name not found");
    }
}
