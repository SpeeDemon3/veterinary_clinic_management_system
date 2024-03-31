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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService((UserDetailsService) userService);

        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilita la protección CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Configura la gestión de sesiones para crear sesiones sin estado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura la autorización de las solicitudes HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite el acceso sin autenticación a las rutas /login y /signup mediante el método POST
                        .requestMatchers(HttpMethod.POST, "/api/user/signup", "/api/user/login").permitAll()
                        // Permite el acceso sin autenticación a las solicitudes GET en la ruta "/api/**".
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        // Permite el acceso sin autenticación a las solicitudes POST en la ruta "/api/**".
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        // Permite el acceso sin autenticación a las solicitudes PUT en la ruta "/api/**".
                        .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
                        // Permite el acceso sin autenticación a las solicitudes DELETE en la ruta "/api/**".
                        .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                        // Requiere autenticación para cualquier otra solicitud
                        .anyRequest().authenticated()
                )
                // Configura el proveedor de autenticación y agrega el filtro de autenticación JWT antes del filtro UsernamePasswordAuthenticationFilter
                .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Construye y devuelve la cadena de filtros de seguridad configurada
        return http.build();
    }

}
