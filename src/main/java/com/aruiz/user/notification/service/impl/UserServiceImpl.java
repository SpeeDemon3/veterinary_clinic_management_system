package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.SignUpRequest;
import com.aruiz.user.notification.controller.dto.UserRequest;
import com.aruiz.user.notification.controller.dto.UserRequestUpdate;
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
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserResponse save(SignUpRequest userRequest) throws Exception {

        UserRequestUpdate userRequestUpdate = modelMapper.map(userRequest, UserRequestUpdate.class);

        userRequestUpdate.setRole(1L);

        UserEntity userEntity = modelMapper.map(userRequest, UserEntity.class);

        userRepository.save(userEntity);

        log.info("Saving entity ID, name {}{}", userEntity.getId(), userEntity.getName());

        return modelMapper.map(userEntity, UserResponse.class);
    }
/*
    public UserResponse save(UserEntity userRequest) throws Exception {

        UserRequestUpdate userRequestUpdate = modelMapper.map(userRequest, UserRequestUpdate.class);

        userRequestUpdate.setRole(1L);

        if(userRequest.getProfile() == null) {
            userRequest.setProfile(null);
        }

        UserEntity userEntity = modelMapper.map(userRequest, UserEntity.class);

        userRepository.save(userEntity);

        log.info("Saving entity ID, name {}{}", userEntity.getId(), userEntity.getName());

        return modelMapper.map(userEntity, UserResponse.class);
    }
*/
    @Override
    public List<UserResponse> findAll() throws Exception {

        List<UserEntity> entityList = userRepository.findAll();

        if (entityList.isEmpty()) {
            log.error("There are no entities to recover!!!");
            throw new Exception();
        }

        List<UserResponse> userResponseList = new ArrayList<>();

        for(UserEntity user : entityList) {
            log.info("Recovering users...");
            userResponseList.add(modelMapper.map(user, UserResponse.class));
            log.info("Info {}", user.toString());
        }

        return userResponseList;
    }

    @Override
    public UserResponse findById(Long id) throws Exception {

        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if (userEntityOptional.isEmpty()) {
            log.error("User not found!!!");
            throw new Exception();
        } else {
            UserEntity userEntity = userEntityOptional.get();
            log.info("User found with ID -> {}", userEntity.getId());

            return modelMapper.map(userEntity, UserResponse.class);
        }


    }

    @Override
    public String deleteById(Long id) throws Exception {

        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User successfully deleted");
            return "User with ID -> " + id + " successfully deleted!!!";
        } else {
            log.error("User not found!!!");
            throw new Exception();
        }

    }

    @Override
    public UserResponse updateById(Long id, UserRequestUpdate userRequest) throws Exception {

        UserEntity userEntity = userRepository.findById(id).orElse(null);

        if (userEntity != null) {

            UserEntity userEntitySave = modelMapper.map(userRequest, UserEntity.class);

            userEntitySave.setId(id);

            userRepository.save(userEntitySave);

            log.info("Entity successfully updated!!!");

            return modelMapper.map(userEntitySave, UserResponse.class);

        } else {
            log.error("User not found!!!");
            throw new Exception();
        }

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
