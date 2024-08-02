package hng_java_boilerplate.organisation.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> generateResponse(String status, String message, Object data, HttpStatus statusCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        response.put("data", data);
        response.put("status_code", statusCode.value());

        return new ResponseEntity<>(response, statusCode);
    }

    public static ResponseEntity<Object> generateErrorResponse(String status, String message, HttpStatus statusCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        response.put("status_code", statusCode.value());

        return new ResponseEntity<>(response, statusCode);
    }
}
