package com.aruiz.user.notification.config;

import com.aruiz.user.notification.filter.JwtAuthenticationFilter;
import com.aruiz.user.notification.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security setup.
 * This class configures authentication, authorization, and JWT token-based authentication filter.
 *
 * @author Antonio Ruiz = speedemon
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Provides the authentication provider using DAO authentication.
     *
     * @return DaoAuthenticationProvider configured with UserDetailsService and PasswordEncoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService((UserDetailsService) userService);

        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }

    /**
     * Retrieves the AuthenticationManager from Spring's AuthenticationConfiguration.
     *
     * @param configuration Spring's AuthenticationConfiguration.
     * @return AuthenticationManager configured.
     * @throws Exception If there's an error retrieving the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http HttpSecurity object to configure security.
     * @return SecurityFilterChain configured with JWT authentication filter.
     * @throws Exception If there's an error configuring HttpSecurity.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disables CSRF protection // Deshabilita la protección CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Configures session management to create stateless sessions // Configura la gestión de sesiones para crear sesiones sin estado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configures authorization for HTTP requests // Configura la autorización de las solicitudes HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Allows unauthenticated access to /login and /signup endpoints via POST method
                        // Permite el acceso sin autenticación a las rutas /login y /signup mediante el método POST
                        .requestMatchers(HttpMethod.POST, "/api/user/signup", "/api/user/login").permitAll()
                        // Allows unauthenticated access to GET requests under /api/**
                        // Permite el acceso sin autenticación a las solicitudes GET en la ruta "/api/**".
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        // Allows unauthenticated access to POST requests under /api/**
                        // Permite el acceso sin autenticación a las solicitudes POST en la ruta "/api/**".
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        // Allows unauthenticated access to PUT requests under /api/**
                        // Permite el acceso sin autenticación a las solicitudes PUT en la ruta "/api/**".
                        .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
                        // Allows unauthenticated access to DELETE requests under /api/**
                        // Permite el acceso sin autenticación a las solicitudes DELETE en la ruta "/api/**".
                        .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                        // Requires authentication for any other request
                        // Requiere autenticación para cualquier otra solicitud
                        .anyRequest().authenticated()
                )
                // Configures authentication provider and adds JWT authentication filter before UsernamePasswordAuthenticationFilter
                // Configura el proveedor de autenticación y agrega el filtro de autenticación JWT antes del filtro UsernamePasswordAuthenticationFilter
                .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Builds and returns the configured security filter chain
        // Construye y devuelve la cadena de filtros de seguridad configurada
        return http.build();
    }

}
