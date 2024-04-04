package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.*;
import com.aruiz.user.notification.domain.Role;
import com.aruiz.user.notification.domain.User;
import com.aruiz.user.notification.entity.ProfileEntity;
import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public JwtResponse signup(SignUpRequest request) throws Exception {

        Optional<Role> roleEntityOptional = Optional.ofNullable(roleServices.findByName("ROLE_USER"));

        Role roleFind;

        RoleEntity roleEntity = new RoleEntity();

        if (roleEntityOptional.isPresent()) {
            roleFind = roleEntityOptional.get();

            roleEntity = modelMapper.map(roleFind, RoleEntity.class);

        }

        // Construir un nuevo objeto UserEntity con los datos proporcionados en la solicitud de registro
        var user = UserEntity
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.setRole(roleEntity.getId()) // EL ROL AL CREAR UN USUARIO SERA ROLE_USER POR DEFECTO
                .profile(modelMapper.map(request.getProfile(), ProfileEntity.class)) //#### TIENE QUE ASIGNAR UN PERFIL, HAY QUE RECUPERARLO Y ASIGNARLO COMO EN ROL
                .build();

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        // Guardar el usuario en la base de datos
        userService.save(user);

        // Generar un token JWT para el usuario registrado
        var jwt = jwtService.generateToken(user);

        // Construir y devolver una respuesta de inicio de sesión con el token JWT generado
        return JwtResponse.builder().token(jwt).build();
    }

    public JwtResponse login(LoginRequest loginRequest) throws Exception {
        // Autenticar al usuario utilizando el AuthenticationManager
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // Buscar al usuario en la base de datos por su dirección de correo electrónico
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        // Generar un token JWT para el usuario que ha iniciado sesión
        var jwt = jwtService.generateToken(user);

        // Construir y devolver una respuesta de inicio de sesión con el token JWT generado
        return JwtResponse.builder().token(jwt).build();
    }

}
