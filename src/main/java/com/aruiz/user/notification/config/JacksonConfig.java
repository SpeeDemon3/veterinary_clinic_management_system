package com.aruiz.user.notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for customizing Jackson ObjectMapper settings.
 *
 * @author Antonio Ruiz = speedemon
 */
@Configuration
public class JacksonConfig {

    /**
     * Configures and returns an ObjectMapper instance with custom settings.
     *
     * @return Configured ObjectMapper instance.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Register JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        // Enable indentation of JSON output for better readability
        // Configura Jackson para que imprima JSON con formato y de manera legible
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

}
