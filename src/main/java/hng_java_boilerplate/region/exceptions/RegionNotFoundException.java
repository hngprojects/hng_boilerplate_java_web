package hng_java_boilerplate.region.exceptions;



public class RegionNotFoundException extends RuntimeException {
    public RegionNotFoundException(String message) {
        super(message);
    }
}
