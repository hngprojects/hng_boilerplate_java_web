package hng_java_boilerplate.validEmail;

public class EmailValidator {
    private static final String EMAIL_COM_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$";

    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_COM_REGEX);
    }
}
