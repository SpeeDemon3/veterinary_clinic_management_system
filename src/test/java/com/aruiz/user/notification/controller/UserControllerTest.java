package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.*;
import com.aruiz.user.notification.domain.Notification;
import com.aruiz.user.notification.domain.Pet;
import com.aruiz.user.notification.domain.Role;
import com.aruiz.user.notification.service.UserService;
import com.aruiz.user.notification.service.impl.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void signup() throws Exception {
        // Give
        SignUpRequest singUpRequest = new SignUpRequest("Antonio", "test@test.es", "password", "88888888F", "666666666", "test.png", "30-07-2024");
        JwtResponse jwtResponse = new JwtResponse();

        // When
        when(authenticationService.signup(any())).thenReturn(jwtResponse);

        // Then
        ResponseEntity<?> responseEntity = userController.signup(singUpRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(jwtResponse, responseEntity.getBody());

        verify(authenticationService, times(1)).signup(any());

    }

    @Test
    void login() throws Exception {

        // Give
        LoginRequest loginRequest = new LoginRequest();

        // Mocking behavior
        when(authenticationService.login(loginRequest)).thenReturn(new JwtResponse());

        // When
        ResponseEntity<?> responseEntity = userController.login(loginRequest);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(authenticationService, times(1)).login(any());

    }

    @Test
    @WithMockUser(roles = {"VETERINARIAN", "ADMIN"})
    void findById() throws Exception {

        // Give
        Long userId = 1L;
        UserResponse userResponse = new UserResponse();

        // Mocking behavior
        when(userService.findById(userId)).thenReturn(new UserResponse());

        // When
        ResponseEntity<?> responseEntity = userController.findById(userId);

        // Then
        assertEquals(200, responseEntity.getStatusCode());

        verify(userService, times(1)).findById(userId);



    }

    @Test
    void findByDni() throws Exception {
        // Give
        String dni = "55555555L";
        UserResponse userResponse = new UserResponse();

        // Mocking behavior
        when(userService.findByDni(dni)).thenReturn(userResponse);

        // When
        ResponseEntity<?> responseEntity = userController.findByDni(dni);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponse, responseEntity.getBody());

        verify(userService, times(1)).findByDni(dni);

    }

    @Test
    void findAll() throws Exception {
        // Give
        List<UserResponse> users = Arrays.asList(new UserResponse(), new UserResponse());

        // Mocking behavior
        when(userService.findAll()).thenReturn(users);

        // When
        ResponseEntity<?> responseEntity = userController.findAll();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users, responseEntity.getBody());

        verify(userService, times(1)).findAll();

    }

    @Test
    void updateById() throws Exception {
        // Give
        Long idUser = 1L;
        UserRequestUpdate userRequestUpdate = new UserRequestUpdate();

        // Mocking behavior
        when(userService.updateById(idUser, userRequestUpdate)).thenReturn(new UserResponse());

        // When
        ResponseEntity<?> responseEntity = userController.updateById(idUser, userRequestUpdate);

        // Then
        assertEquals(200, responseEntity.getStatusCode());

    }

    @Test
    void deleteById() throws Exception {
        // Give
        long userId = 1L;

        // Mocking behavior
        when(userService.deleteById(userId)).thenReturn(true);

        // When
        ResponseEntity<?> responseEntity = userController.deleteById(userId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(userService, times(1)).deleteById(userId);

    }

    @Test
    void addUserImg() {
        // Give

        // Mocking behavior

        // When

        // Then

    }

    @Test
    void getUserImg() {
        // Give

        // Mocking behavior

        // When

        // Then

    }

    @Test
    void downloadFileCsvUsers() {
        // Give

        // Mocking behavior

        // When

        // Then

    }

    @Test
    void downloadFileJsonPets() {
        // Give

        // Mocking behavior

        // When

        // Then

    }

    @Test
    void updateRoleUser() {
        // Give

        // Mocking behavior

        // When

        // Then

    }
}