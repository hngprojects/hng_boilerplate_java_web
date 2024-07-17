package hng_java_boilerplate.welcome;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WelcomeControllerTest {
    @Test
    public void HelloWorldShouldReturnHelloWorld() {
        var controller = new WelcomeController();

        assertEquals(controller.HelloWorld(), "Hello world");
    }
}
