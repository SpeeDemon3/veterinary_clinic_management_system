package com.aruiz.user.notification.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for creating and configuring a ModelMapper instance.
 */
@Configuration
public class AppConfig {

    /**
     * Creates and returns a new ModelMapper instance.
     *
     * @return ModelMapper instance configured for mapping objects.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
