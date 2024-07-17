package hng_java_boilerplate.welcome;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping
    public String HelloWorld() {
        return "Hello world";
    }
}
