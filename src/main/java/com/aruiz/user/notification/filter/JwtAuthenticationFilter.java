package com.aruiz.user.notification.filter;

import com.aruiz.user.notification.service.impl.JwtService;
import com.aruiz.user.notification.service.impl.UserServiceImpl;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * JWT Authentication Filter for authenticating and authorizing requests based on JWT tokens.
 *
 * @author Antonio Ruiz = speedemon
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceImpl userService;


    /**
     * Filter method to perform JWT authentication and authorization.
     *
     * @param request HTTP servlet request.
     * @param response HTTP servlet response.
     * @param filterChain Filter chain for chaining multiple filters.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        log.info("Request URL: " + request.getRequestURI());
        log.info("Request Method: " + request.getMethod());
        log.info("Authorization Header: " + request.getHeader("Authorization"));

        if (isSignupRequest(request)) {
            log.error("Authorization header is missing or does not start with Bearer");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // Preuba
            filterChain.doFilter(request, response);
            return;
        }


        // Verificar si la solicitud es para crear un usuario y permitirlo sin JWT
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            if (isSignupRequest(request)) {
                log.error("Authorization header is missing or does not start with Bearer");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // Preuba
                filterChain.doFilter(request, response);
                return;
            } else if (isLoginRequest(request) || !isSignupRequest(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            log.error("Authorization header is missing or does not start with Bearer");
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;


        }

        jwt = authHeader.substring(7);

        if (StringUtils.isEmpty(jwt)) {
            log.error("JWT token is missing in the Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        log.info("JWT -> {}", jwt);

        try {
            userEmail = jwtService.extractUserName(jwt);
            UserDetails userDetails = userService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Establecer la autenticación en el contexto de seguridad
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                log.error("JWT token is not valid for user: {}", userEmail);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception e) {
            log.error("Error processing JWT: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(userEmail);

            // Verify if the JWT token is valid for the current user
            // Verificamos si el token JWT es válido para el usuario actual
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("User - {}", userDetails);

                // Create an empty security context
                // Creamos un contexto de seguridad vacío
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                // Create an authentication object using user details and roles
                // Creamos un objeto de autenticación utilizando los detalles del usuario y los roles
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // Set authentication details
                // Establecemos los detalles de la autenticación
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set authentication in the security context
                // Establecemos la autenticación en el contexto de seguridad
                securityContext.setAuthentication(authenticationToken);
                // Set security context in the current context
                // Establecemos el contexto de seguridad en el contexto actual
                SecurityContextHolder.setContext(securityContext);
            } else {
                log.error("JWT token is not valid for user: {}", userEmail);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        }
        // Continue with the filter chain
        // Continuamos con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Metodo de prueba
    private boolean isSignupRequest(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.POST.name()) && request.getRequestURI().equals("/api/user/signup");
    }
    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.POST.name()) && request.getRequestURI().equals("/api/user/login");
    }
}
