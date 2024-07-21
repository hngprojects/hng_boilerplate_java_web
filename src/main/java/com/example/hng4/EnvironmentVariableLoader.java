package com.example.hng4;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class EnvironmentVariableLoader {

    private final Environment environment;

    public EnvironmentVariableLoader(Environment environment) {
        this.environment = environment;
        loadEnvironmentVariables();
    }

    private void loadEnvironmentVariables() {
        String apiKey = System.getenv("TYPESENSE_API_KEY");
        String node = System.getenv("TYPESENSE_NODE");

        if (apiKey == null || node == null) {
            // If environment variables are not set, try to load from .env file
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(".env")) {
                props.load(fis);
                apiKey = props.getProperty("TYPESENSE_API_KEY");
                node = props.getProperty("TYPESENSE_NODE");
            } catch (IOException e) {
                System.err.println("Failed to load .env file: " + e.getMessage());
            }
        }

        if (apiKey != null && node != null) {
            System.setProperty("TYPESENSE_API_KEY", apiKey);
            System.setProperty("TYPESENSE_NODE", node);
        } else {
            System.err.println("Failed to load Typesense configuration. Please set TYPESENSE_API_KEY and TYPESENSE_NODE environment variables or provide them in a .env file.");
        }
    }
}