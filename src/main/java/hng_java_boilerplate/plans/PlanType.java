package hng_java_boilerplate.plans;

public enum PlanType {

    FREE("FREE"), BASIC("BASIC"), PREMIUM("PREMIUM"), ADVANCED("ADVANCED");

    private final String name;
    PlanType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
