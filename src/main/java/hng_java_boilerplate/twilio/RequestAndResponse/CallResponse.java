package hng_java_boilerplate.twilio.RequestAndResponse;

public class CallResponse {
    private String message;
    private String callSid;

    public CallResponse(String message, String callSid){
        this.message = message;
        this.callSid = callSid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallSid() {
        return callSid;
    }

    public void setCallSid(String callSid) {
        this.callSid = callSid;
    }
}
