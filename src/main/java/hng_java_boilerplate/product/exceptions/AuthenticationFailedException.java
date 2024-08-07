package hng_java_boilerplate.product.exceptions;

public class AuthenticationFailedException extends RuntimeException{

    String message;
    public AuthenticationFailedException(String message){this.message = message;}
}
