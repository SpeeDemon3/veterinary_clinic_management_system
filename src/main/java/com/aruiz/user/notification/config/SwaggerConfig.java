package com.aruiz.user.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Veterinary Clinic Management System")
                        .version("1.0")
                        .description("API to manage a veterinary clinic, from users, roles, pets, clients, appointments and invoices.")
                        .contact(new Contact()
                                .name("Antonio Ruiz")
                                .url("https://github.com/SpeeDemon3")
                        )
                );
    }
}
