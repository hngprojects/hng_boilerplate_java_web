package hng_java_boilerplate.email.exception;

public class EmailTemplateNotFound extends RuntimeException{
    public EmailTemplateNotFound(String message) {
        super(message);
    }
}
