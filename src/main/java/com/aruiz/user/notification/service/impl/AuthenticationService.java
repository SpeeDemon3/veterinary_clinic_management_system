package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.*;
import com.aruiz.user.notification.domain.Role;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Authentication Service
 *
 * @author Antonio Ruiz = speedemon
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final RoleServicesImpl roleServices;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Registers a new user based on the provided sign-up request and generates a JWT token upon successful registration.
     *
     * @param request The sign-up request containing user details (name, email, password, etc.).
     * @return JwtResponse containing the generated JWT token.
     * @throws Exception If user registration fails due to validation errors or other exceptions.
     */
    public JwtResponse signup(SignUpRequest request) throws Exception {

        // Retrieve the default role (assuming role with ID 1)
        Optional<RoleResponse> roleEntityOptional = Optional.ofNullable(roleServices.findById(1L));
        RoleEntity roleFind;

        // Map the retrieved role to RoleEntity
        RoleEntity roleEntity = new RoleEntity();

        ArrayList<PetEntity> pets = new ArrayList<>();

        if (roleEntityOptional.isPresent()) {
            roleFind = modelMapper.map(roleEntityOptional.get(), RoleEntity.class);

            roleEntity = modelMapper.map(roleFind, RoleEntity.class);


            //log.info(String.valueOf(request.getProfile()));

            //profileEntity = modelMapper.map(request.getProfile(), ProfileEntity.class);

            // Create a new UserEntity object with the data provided in the sign-up request
            // Construir un nuevo objeto UserEntity con los datos proporcionados en la solicitud de registro
            var user = UserEntity
                    .builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .dni(request.getDni())
                    .phoneNumber(request.getPhoneNumber())
                    .img(request.getImg())
                    .birthdate(request.getBirthdate())
                    .role(roleEntity)
                    .pets(null) // Assuming no pets are added during sign-up
                    .build();

            // Save the user to the database
            // Guardar el usuario en la base de datos
            userService.saveEntity(user);

            log.info(String.valueOf(user));


            // Generate a JWT token for the registered user
            // Generar un token JWT para el usuario registrado
            var jwt = jwtService.generateToken(user);

            // Build and return a JwtResponse containing the generated token
            // Construir y devolver una respuesta de inicio de sesión con el token JWT generado
            return JwtResponse.builder().token(jwt).build();

        }

        log.error("User creation failed!!!!");
        return null;

    }

    /**
     * Authenticates a user based on the provided credentials and generates a JWT token upon successful authentication.
     *
     * @param loginRequest The login request containing user credentials (email and password).
     * @return JwtResponse containing the generated JWT token.
     * @throws Exception If authentication fails due to invalid credentials or other errors.
     */
    public JwtResponse login(LoginRequest loginRequest) throws Exception {
        // Authenticate the user using the AuthenticationManager (provided by Spring Security)
        // Autenticar al usuario utilizando el AuthenticationManager
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // Retrieve the user details from the database based on email
        // Buscar al usuario en la base de datos por su dirección de correo electrónico
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        // Generate a JWT token for the authenticated user
        // Generar un token JWT para el usuario que ha iniciado sesión
        var jwt = jwtService.generateToken(user);

        // Build and return a JwtResponse containing the generated token
        // Construir y devolver una respuesta de inicio de sesión con el token JWT generado
        return JwtResponse.builder().token(jwt).build();
    }

}
