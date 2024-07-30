package hng_java_boilerplate.payment.enums;

public enum PaymentProvider {

    PAYSTACK("PAYSTACK"), FLUTTERWAVE("FLUTTERWAVE");

    private final String name;

    PaymentProvider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
