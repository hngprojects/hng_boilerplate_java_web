package hng_java_boilerplate.dtos.requests;

import java.util.List;

public class DeleteRecoveryOptionsRequest {

    private List<String> options;

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

}
