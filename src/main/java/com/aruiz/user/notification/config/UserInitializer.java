package com.aruiz.user.notification.config;

import com.aruiz.user.notification.controller.dto.SignUpRequest;
import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.RoleRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.impl.AuthenticationService;
import com.aruiz.user.notification.service.impl.UserServiceImpl;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Component responsible for initializing the application with default admin user if not present.
 * It creates the admin user based on predefined parameters and assigns the "ROLE_ADMIN" role.
 *
 * @author Antonio Ruiz = speedemon
 */
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

    /**
     * Initializes the application by creating an admin user if not already present in the database.
     * This method is executed after the bean is constructed.
     *
     * @throws Exception If there are issues while creating the admin user or assigning roles.
     */
    @PostConstruct
    public void init() throws Exception {

        final String name = "Antonio";
        final String email = "admin@test.com";
        final String password = "pass";
        final String dni = "11223344T";
        final String phoneNumber = "+34 666666666";
        final String img = "never.png";
        final String birthdate = "2024-01-01";

        createUserAdmin(name, email, password, dni, phoneNumber, img, birthdate);

    }

    /**
     * Creates an admin user with the specified details if not already present.
     *
     * @param name Name of the admin user.
     * @param email Email address of the admin user.
     * @param password Password of the admin user.
     * @param dni DNI (identity document number) of the admin user.
     * @param phoneNumber Phone number of the admin user.
     * @param img Image URL or path of the admin user.
     * @param birthdate Birthdate of the admin user.
     * @throws Exception If there are issues while creating the admin user or assigning roles.
     */
    private void createUserAdmin(String name, String email, String password, String dni,
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

    /**
     * Updates the role of a user based on their DNI.
     *
     * @param user The user entity whose role needs to be updated.
     * @throws Exception If there are issues while updating the user's role.
     */
    private void updateRoleUser(UserEntity user) throws Exception {
        userRepository.findByDni(user.getDni());
        userService.updateRoleByDni(user.getDni(), 3L); // Assuming 3L represents a specific role ID.
    }



}
