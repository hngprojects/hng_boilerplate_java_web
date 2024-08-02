package hng_java_boilerplate.email.exception;

public class EmailTemplateExists extends RuntimeException{
    public EmailTemplateExists(String message) {
        super(message);
    }
}
