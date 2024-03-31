package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.UserRequest;
import com.aruiz.user.notification.controller.dto.UserResponse;
import com.aruiz.user.notification.domain.User;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserResponse save(UserRequest userRequest) throws Exception {

        UserEntity userEntity = modelMapper.map(userRequest, UserEntity.class);

        userRepository.save(userEntity);

        log.info("Saving entity ID, name {}{}", userEntity.getId(), userEntity.getName());

        return modelMapper.map(userEntity, UserResponse.class);
    }

    public UserResponse save(UserEntity userEntity) throws Exception {

        userRepository.save(userEntity);

        log.info("Saving entity ID, name {}{}", userEntity.getId(), userEntity.getName());

        return modelMapper.map(userEntity, UserResponse.class);
    }

    @Override
    public List<UserResponse> findAll() throws Exception {

        List<UserEntity> entityList = userRepository.findAll();

        if (entityList.isEmpty()) {
            log.error("There are no entities to recover!!!");
            return null;
        }

        List<UserResponse> userResponseList = new ArrayList<>();

        for(UserEntity user : entityList) {
            userResponseList.add(modelMapper.map(user, UserResponse.class));
        }

        return userResponseList;
    }

    @Override
    public UserResponse findById(Long id) throws Exception {

        UserEntity userEntity = userRepository.findById(id).orElse(null);

        if (userEntity == null) {
            log.error("User not found");
            return null;
        }

        log.info("User found with ID -> {}", userEntity.getId());

        return modelMapper.map(userEntity, UserResponse.class);
    }

    @Override
    public String deleteById(Long id) throws Exception {

        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User successfully deleted");
            return "User with ID -> " + id + "successfully deleted";
        }

        return null;
    }

    @Override
    public UserResponse updateById(Long id, UserRequest userRequest) throws Exception {

        UserEntity userEntity = userRepository.findById(id).orElse(null);

        if (userEntity != null) {

            UserEntity userEntitySave = modelMapper.map(userRequest, UserEntity.class);

            userEntitySave.setId(id);

            userRepository.save(userEntitySave);

            log.info("Entity successfully updated!!!");

            return modelMapper.map(userEntitySave, UserResponse.class);

        }

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String emailUser) {
        // Buscamos el usuario en la base de datos por su correo electrónico
        try {
            return userRepository.findByEmail(emailUser)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + emailUser));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
