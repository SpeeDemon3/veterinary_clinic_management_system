package com.aruiz.user.notification.config;

import com.aruiz.user.notification.controller.dto.SignUpRequest;
import com.aruiz.user.notification.controller.dto.UserResponse;
import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.RoleRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.UserService;
import com.aruiz.user.notification.service.impl.AuthenticationService;
import com.aruiz.user.notification.service.impl.UserServiceImpl;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserServiceImpl userService;

    @PostConstruct
    public void init() throws Exception {

        final String name = "Antonio";
        final String email = "admin@test.com";
        final String password = "pass";
        final String dni = "11223344T";
        final String phoneNumber = "+34 666666666";
        final String img = "never.png";
        final String birthdate = "2024-01-01";

        createUser(name, email, password, dni, phoneNumber, img, birthdate);

    }

    private void createUser(String name, String email, String password, String dni,
                            String phoneNumber, String img, String birthdate) throws Exception {

        try {
            Optional<UserEntity> optionalUserEntity = userRepository.findByDni(dni);
            Optional<RoleEntity> optionalRoleRepository = roleRepository.findByName("ROLE_ADMIN");

            if (!optionalUserEntity.isPresent() && optionalRoleRepository.isPresent()) {

                UserEntity userEntity = UserEntity.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .dni(dni)
                        .phoneNumber(phoneNumber)
                        .img(img)
                        .birthdate(birthdate)
                        .role(optionalRoleRepository.get())
                        .build();

                authenticationService.signup(modelMapper.map(userEntity, SignUpRequest.class));

                updateRoleUser(userEntity);

                System.out.println("Admin -> " + userEntity.getName() + " was created automatically.");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }


    private void updateRoleUser(UserEntity user) throws Exception {
        userRepository.findByDni(user.getDni());
        userService.updateRoleByDni(user.getDni(), 3L);
    }



}
