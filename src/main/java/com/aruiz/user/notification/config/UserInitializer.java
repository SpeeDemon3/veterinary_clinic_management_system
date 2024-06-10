package com.aruiz.user.notification.config;

import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.RoleRepository;
import com.aruiz.user.notification.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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

        Optional<UserEntity> optionalUserEntity = userRepository.findByDni(dni);
        Optional<RoleEntity> optionalRoleRepository = roleRepository.findByName("ROLE_ADMIN");

        if (!optionalUserEntity.isPresent()) {

            UserEntity userEntity = UserEntity.builder()
                    .name(name)
                    .email(email)
                    .password(password)
                    .dni(dni)
                    .phoneNumber(phoneNumber)
                    .img(img)
                    .birthdate(birthdate)
                    .role(optionalRoleRepository.get())
                    .notifications(null)
                    .pets(null)
                    .appointments(null)
                    .build();

                userRepository.save(userEntity);

            System.out.println("Admin -> " + userEntity.getName() + " was created automatically.");
        }

    }

}
