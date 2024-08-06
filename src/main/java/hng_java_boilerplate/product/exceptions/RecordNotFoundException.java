package hng_java_boilerplate.product.exceptions;
public class RecordNotFoundException extends RuntimeException{

    String message;

    public RecordNotFoundException(String message) {
        this.message = message;
    }
}
