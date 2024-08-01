package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.SignUpRequest;
import com.aruiz.user.notification.controller.dto.UserRequestUpdate;
import com.aruiz.user.notification.controller.dto.UserResponse;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.RoleRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void save() throws Exception {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest();
        UserEntity userEntity = new UserEntity();
        UserResponse userResponse = new UserResponse();

        // Mocking behavior
        when(modelMapper.map(signUpRequest, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(userResponse);

        // When
        UserResponse responseSave = userService.save(signUpRequest);

        // Then
        assertEquals(userResponse, responseSave);

    }

    @Test
    void findAll() throws Exception {
        // Given
        List<UserEntity> userEntityList = new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        userEntityList.add(userEntity);

        List<UserResponse> userResponseList = new ArrayList<>();
        userResponseList.add(new UserResponse());

        // Mocking behavior
        when(userRepository.findAll()).thenReturn(userEntityList);
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(new UserResponse());

        // When
        List<UserResponse> userResponses = userService.findAll();

        // Then
        assertEquals(userEntityList.size(), userResponses.size());
        assertEquals(userEntityList.get(0).getId(), userResponses.get(0).getId());

    }

    @Test
    void findById() throws Exception {
        // Given
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        UserResponse userResponseMock = new UserResponse();
        userResponseMock.setId(userId);

        // Mocking behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(userResponseMock);

        // When
        UserResponse userResponse = userService.findById(userId);

        // Then
        assertEquals(userResponseMock.getId(), userResponse.getId());

    }

    @Test
    void findByDni() throws Exception {
        // Give
        String dni = "55553333A";
        UserEntity userEntity = new UserEntity();
        userEntity.setDni(dni);

        UserResponse userResponseMock = new UserResponse();
        userResponseMock.setDni(dni);

        // Mocking behavior
        when(userRepository.findByDni(dni)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(userResponseMock);

        // When
        UserResponse userResponse;
        userResponse = userService.findByDni(dni);

        // Then
        assertEquals(userResponseMock.getDni(), userResponse.getDni());

    }

    @Test
    void findByEmail() throws Exception {
        // Given
        String email = "test@test.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        UserResponse userResponseMock = new UserResponse();
        userResponseMock.setEmail(email);

        // Mocking behavior
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(userResponseMock);

        // When
        UserResponse userResponse = userService.findByEmail(email);

        // Then
        assertEquals(userResponseMock.getEmail(), userResponse.getEmail());

    }

    @Test
    void deleteById() throws Exception {
        // Given
        Long userId = 3L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        // Mocking behavior
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        // When
        boolean result = userService.deleteById(userId);

        // Then
        assertEquals(true, result);
        verify(userRepository, times(1)).deleteById(userId);

    }

    @Test
    void updateById() throws Exception {
        // Given
        Long idUser = 1L;
        UserRequestUpdate userRequestUpdate = new UserRequestUpdate();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(idUser);
        UserResponse userResponse = new UserResponse();

        // Mocking behavior
        when(userRepository.findById(idUser)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userRequestUpdate, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(userResponse);

        // When
        UserResponse userResponseUpdate = userService.updateById(idUser, userRequestUpdate);

        // Then
        assertEquals(userResponse, userResponseUpdate);


    }
}