package com.aruiz.user.notification.config;

import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleInitializer {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Initializes the system by creating a default user role if it doesn't exist.
     *
     * @throws Exception if there is an error during initialization.
     */
    @PostConstruct
    public void init() throws Exception {

        final String roleName = "ROLE_USER";
        final String description = "unprivileged user role";

        Optional<RoleEntity> entityOptional = roleRepository.findByName(roleName);

        if (!entityOptional.isPresent()) {

            RoleEntity role = new RoleEntity();

            role.setName(roleName);
            role.setDescription(description);

            roleRepository.save(role);

            System.out.println("The role -> " + roleName + " was created automatically.");

        }

    }

}
