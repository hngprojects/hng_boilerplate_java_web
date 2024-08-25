package hng_java_boilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Program {

	public static void main(String[] args) {
		SpringApplication.run(Program.class, args);
	}

}
