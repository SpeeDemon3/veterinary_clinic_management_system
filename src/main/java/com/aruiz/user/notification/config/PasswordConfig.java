package com.aruiz.user.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for Password Encoder setup.
 * Provides a BCryptPasswordEncoder bean to encode passwords.
 *
 * @author Antonio Ruiz = speedemon
 */
@Configuration
public class PasswordConfig {
    /**
     * Provides a BCryptPasswordEncoder bean.
     *
     * @return BCryptPasswordEncoder bean instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
