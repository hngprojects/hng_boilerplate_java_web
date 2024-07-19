package hng_java_boilerplate.waitlist.controller;

import hng_java_boilerplate.waitlist.entity.Waitlist;
import hng_java_boilerplate.waitlist.service.WaitlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/waitlist")
public class WaitlistController {
    @Autowired
    private WaitlistService waitlistService;

    @PostMapping
    public ResponseEntity<?> createWaitlist(@Valid @RequestBody Waitlist waitlist){
        waitlistService.saveWaitlist(waitlist);
        Map<String, String> response = new HashMap<>();
        response.put("message", "You are all signed up!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
