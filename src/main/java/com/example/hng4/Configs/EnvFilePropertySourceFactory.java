package com.example.hng4.Configs;

import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

public class EnvFilePropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        Properties props = new Properties();
        props.load(resource.getInputStream());
        return new PropertiesPropertySource(resource.getResource().getFilename(), props);
    }
}
