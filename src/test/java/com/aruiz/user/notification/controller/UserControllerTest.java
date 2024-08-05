package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.*;
import com.aruiz.user.notification.service.impl.AuthenticationService;
import com.aruiz.user.notification.service.impl.JwtService;
import com.aruiz.user.notification.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtService jwtService;

    @Test
    void signup() throws Exception {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest();
        JwtResponse jwtResponseMock = new JwtResponse();
        jwtResponseMock.setToken("mockToken");

        // Mocking behavior
        when(authenticationService.signup(signUpRequest)).thenReturn(jwtResponseMock);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(jwtResponseMock.getToken()));
    }

    @Test
    void login() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        JwtResponse jwtResponseMock = new JwtResponse();
        jwtResponseMock.setToken("mockToken");

        // Mocking behavior
        when(authenticationService.login(loginRequest)).thenReturn(jwtResponseMock);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(jwtResponseMock.getToken()));
    }

    @Test
    void findById() throws Exception {
        // Give
        Long userId = 3L;
        UserResponse userResponseMock = new UserResponse();
        userResponseMock.setId(userId);
        userResponseMock.setName("Test");

        // Mocking behavior
        when(userService.findById(userId)).thenReturn(userResponseMock);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/findById/{id}", userId)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Test"));

    }

    @Test
    void findById_BadRequest() throws Exception {
        // Given
        Long userId = 1L;
        when(userService.findById(userId)).thenThrow(new BadRequestException("Bad request"));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/findById/{id}", userId)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    void findByDni() {
        // Give
        // Mocking behavior
        // When
        // Then
    }

    @Test
    void findAll() throws Exception {
        // Given
        UserResponse userResponse1 = new UserResponse();
        userResponse1.setId(1L);
        userResponse1.setName("Test_1");

        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(2L);
        userResponse2.setName("Test_2");

        List<UserResponse> userList = Arrays.asList(userResponse1, userResponse2);

        when(userService.findAll()).thenReturn(userList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/findAll")
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test_1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Test_2"));
    }

    @Test
    void findAll_NotFound() throws Exception {
        // Given
        when(userService.findAll()).thenThrow(new EntityNotFoundException("Users not found"));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/findAll")
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void findAll_InternalServerError() throws Exception {
        // Given
        when(userService.findAll()).thenThrow(new RuntimeException("Unexpected error"));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/findAll")
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isInternalServerError())
                .andExpect(content().string(""));
    }


    @Test
    void updateById() throws Exception {
        // Given
        Long userId = 1L;
        UserRequestUpdate userRequest = new UserRequestUpdate();
        userRequest.setName("Updated Name");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setName("Updated Name");

        when(userService.updateById(userId, userRequest)).thenReturn(userResponse);

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/user/updateById/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.name").value(userResponse.getName()));
    }

    @Test
    void updateById_NotFound() throws Exception {
        // Given
        Long userId = 1L;
        UserRequestUpdate userRequest = new UserRequestUpdate();
        userRequest.setName("Updated Name");

        when(userService.updateById(userId, userRequest)).thenThrow(new EntityNotFoundException("User not found"));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/updateById/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
        );

        // Then
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void updateById_BadRequest() throws Exception {
        // Given
        Long userId = 1L;
        UserRequestUpdate userRequest = new UserRequestUpdate();
        userRequest.setName("Updated Name");

        when(userService.updateById(userId, userRequest)).thenThrow(new BadRequestException("Invalid data"));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/user/updateById/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
        );

        // Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    void updateById_InternalServerError() throws Exception {
        // Given
        Long userId = 1L;
        UserRequestUpdate userRequest = new UserRequestUpdate();
        userRequest.setName("Updated Name");

        when(userService.updateById(userId, userRequest)).thenThrow(new RuntimeException("Unexpected error"));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/user/updateById/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
        );

        // Then
        resultActions.andExpect(status().isInternalServerError())
                .andExpect(content().string(""));
    }

    @Test
    void deleteById() throws Exception {
        // Given
        Long userId = 1L;

        // Mocking behavior
        when(userService.deleteById(userId)).thenReturn(true);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/user/deleteById/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));
    }

    @Test
    void addUserImg() throws Exception {
        // Given
        Long userId = 1L;
        MultipartFile imageFile = new MockMultipartFile("imageFile", "test.png", "image/png", "some image content".getBytes());

        // Mocking behavior
        doNothing().when(userService).addUserImg(userId, imageFile);

        // When
        ResultActions resultActions = mockMvc.perform(multipart("/api/user/userImg/{id}/add", userId)
                .file((MockMultipartFile) imageFile)
                .param("imageFile", "test.png")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Image successfully saved!!!"));
    }

    @Test
    void addUserImg_BadRequest_NotPng() throws Exception {
        // Given
        Long userId = 1L;
        MultipartFile imageFile = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "some image content".getBytes());

        // When
        ResultActions resultActions = mockMvc.perform(multipart("/api/user/userImg/{id}/add", userId)
                .file((MockMultipartFile) imageFile)
                .param("imageFile", "test.jpg")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void addUserImg_InternalServerError() throws Exception {
        // Given
        Long userId = 1L;
        MultipartFile imageFile = new MockMultipartFile("imageFile", "test.png", "image/png", "some image content".getBytes());

        // Mocking behavior
        doThrow(new RuntimeException("Internal server error")).when(userService).addUserImg(userId, imageFile);

        // When
        ResultActions resultActions = mockMvc.perform(multipart("/api/user/userImg/{id}/add", userId)
                .file((MockMultipartFile) imageFile)
                .param("imageFile", "test.png")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void getUserImg() throws Exception {
        // Given
        Long userId = 1L;
        byte[] imageBytes = "dummy image content".getBytes(); // Simula el contenido de la imagen

        // Mocking behavior
        when(userService.getUserImg(userId)).thenReturn(imageBytes);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/userImg/{id}", userId)
                .accept(MediaType.IMAGE_PNG)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageBytes));
    }

    @Test
    void getUserImg_NotFound() throws Exception {
        // Given
        Long userId = 1L;

        // Mocking behavior
        when(userService.getUserImg(userId)).thenThrow(new EntityNotFoundException("User not found"));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/userImg/{id}", userId)
                .accept(MediaType.IMAGE_PNG)
        );

        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void getUserImg_BadRequest() throws Exception {
        // Given
        Long userId = 1L;

        // Mocking behavior
        when(userService.getUserImg(userId)).thenThrow(new BadRequestException("Bad request"));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/userImg/{id}", userId)
                .accept(MediaType.IMAGE_PNG)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void getUserImg_InternalServerError() throws Exception {
        // Given
        Long userId = 1L;

        // Mocking behavior
        when(userService.getUserImg(userId)).thenThrow(new RuntimeException("Internal server error"));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/userImg/{id}", userId)
                .accept(MediaType.IMAGE_PNG)
        );

        // Then
        resultActions.andExpect(status().isInternalServerError());
    }

    @Test
    void downloadFileCsvUsers() throws Exception {
        // Given
        String csvContent = "id,name\n1,Test 1\n2,Test 2";
        byte[] csvBytes = csvContent.getBytes();

        // Mocking behavior
        when(userService.usersInfoDownloadCsv()).thenReturn(csvContent);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/downloadFileCsvUsers")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"users-data.csv\""))
                .andExpect(content().bytes(csvBytes));
    }

    @Test
    void downloadFileJsonUsers_InternalServerError() throws Exception {
        // Given
        when(userService.usersInfoDownloadJson()).thenThrow(new RuntimeException("Error generating JSON"));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/downloadFileJsonUsers")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isInternalServerError())
                .andExpect(content().string("Error downloading pets JSON!!!!"));
    }

    @Test
    void downloadFileJsonUsers() throws Exception {
        // Given
        String jsonContent = "[{\"id\":1,\"name\":\"John Doe\"},{\"id\":2,\"name\":\"Jane Doe\"}]";
        byte[] jsonBytes = jsonContent.getBytes(StandardCharsets.UTF_8);

        // Mocking behavior
        when(userService.usersInfoDownloadJson()).thenReturn(Arrays.toString(jsonBytes));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/user/downloadFileJsonUsers")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"users.json\""))
                .andExpect(content().bytes(jsonBytes));
    }

    @Test
    void updateRoleUser() throws Exception {
        // Given
        String dni = "12345678A";
        Long idRole = 2L;
        UserResponse userResponse = new UserResponse();

        // Mocking behavior
        when(userService.updateRoleByDni(dni, idRole)).thenReturn(userResponse);

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/user/updateRoleUser/{dni}/{idRole}", dni, idRole)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userResponse.getId()));
    }

    @Test
    void updateRoleUser_BadRequest() throws Exception {
        // Given
        String dni = "12345678A";
        Long idRole = 2L;

        // Mocking behavior
        when(userService.updateRoleByDni(dni, idRole)).thenThrow(new BadRequestException("Bad request"));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/user/updateRoleUser/{dni}/{idRole}", dni, idRole)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void updateRoleUser_NotFound() throws Exception {
        // Given
        String dni = "12345678A";
        Long idRole = 2L;

        // Mocking behavior
        when(userService.updateRoleByDni(dni, idRole)).thenThrow(new EntityNotFoundException("User not found"));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/user/updateRoleUser/{dni}/{idRole}", dni, idRole)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void updateRoleUser_InternalServerError() throws Exception {
        // Given
        String dni = "12345678A";
        Long idRole = 2L;

        // Mocking behavior
        when(userService.updateRoleByDni(dni, idRole)).thenThrow(new RuntimeException("Internal server error"));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/user/updateRoleUser/{dni}/{idRole}", dni, idRole)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isInternalServerError());
    }

}