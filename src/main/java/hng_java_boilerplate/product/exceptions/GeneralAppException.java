package hng_java_boilerplate.product.exceptions;

public class GeneralAppException extends RuntimeException{

    String message;

    public GeneralAppException(String message){this.message = message;}
}
