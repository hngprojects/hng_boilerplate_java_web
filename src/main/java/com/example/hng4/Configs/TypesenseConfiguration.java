package com.example.hng4.Configs;

import java.time.Duration;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.typesense.api.Client;
import org.typesense.resources.Node;

import jakarta.annotation.PostConstruct;




@Configuration
@PropertySource(value = "file:.env", factory = EnvFilePropertySourceFactory.class, ignoreResourceNotFound = true)
public class TypesenseConfiguration {

    @Value("${TYPESENSE_API_KEY:}")
    private String apiKey;

    @Value("${TYPESENSE_NODE:}")
    private String nodeUrl;

    public TypesenseConfiguration() {
        System.out.println("TypesenseConfiguration constructor called");
    }

    // Add this method to print values after properties are set
    @PostConstruct
    public void init() {
        System.out.println("API Key: " + apiKey);
        System.out.println("Node URL: " + nodeUrl);
    }

    @Bean
    public Client typesenseClient() {

        if (apiKey.isEmpty() || nodeUrl.isEmpty()) {
            throw new IllegalStateException("Typesense API key or node URL is not set. Please check your .env file or environment variables.");
        }

        String[] nodeparts = nodeUrl.split(":");
        String host = nodeparts[0];
        String port = nodeparts[1];

        Node node = new Node("https", host, port);
     // Create a Node instance
     // Create a Node instance with the correct parameters
    //  Node node = new Node("https", "9fiu6e3an7zh8cmkp-1.a1.typesense.net", "443");

        
    //     org.typesense.api.Configuration config;
    //     config = new org.typesense.api.Configuration(
    //         Collections.singletonList(node), // List of nodes
    //         Duration.ofSeconds(5),          // Timeout for requests
    //         "W5bFgG7fWZA3DHhPNbA71JhyPxtycWJA"        // API Key
    // );
    //     return new Client(config);

    org.typesense.api.Configuration config = new org.typesense.api.Configuration(
            Collections.singletonList(node),
            Duration.ofSeconds(5),
            apiKey
        );

        return new Client(config);
    }
}
