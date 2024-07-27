package hng_java_boilerplate.payment.enums;

public enum PaymentStatus {

    PENDING("PENDING"), PAID("PAID"), ABANDONED("ABANDONED"), FAILED("FAILED"), PROCESSING("PROCESSING"), REVERSED("REVERSED"), UNKNOWN("UNKNOWN");

    private final String name;

    PaymentStatus(String name) {
        this.name = name;
    }

    private String getName() {
        return name;
    }

}
