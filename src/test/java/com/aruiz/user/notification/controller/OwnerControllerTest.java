package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.OwnerRequest;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.service.impl.AuthenticationService;
import com.aruiz.user.notification.service.impl.JwtService;
import com.aruiz.user.notification.service.impl.OwnerServiceImp;
import com.aruiz.user.notification.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad
class OwnerControllerTest {

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

    @MockBean
    private OwnerServiceImp ownerServiceImp;

    @Test
    void addOwner() throws Exception {
        // Give
        Long petId = 1L;
        OwnerRequest ownerRequest = new OwnerRequest();

        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setId(1L);

        // Mocking behavior
        when(ownerServiceImp.save(petId, ownerRequest)).thenReturn(ownerResponse);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/owner/add/{petId}", petId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ownerRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ownerResponse.getId()));

    }

    @Test
    void findByEmail() throws Exception {
        // Give
        String email = "test@test.com";
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setEmail(email);

        // Mocking behavior
        when(ownerServiceImp.findByEmail(email)).thenReturn(ownerResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/owner/findByEmail/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(ownerResponse.getEmail()));

    }

    @Test
    void findByDni() throws Exception {
        // Give
        String dni = "33333333T";
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setDni(dni);

        // Mocking behavior
        when(ownerServiceImp.findByDni(dni)).thenReturn(ownerResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/owner/findByDni/{dni}", dni)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dni").value(ownerResponse.getDni()));

    }

    @Test
    void findById() throws Exception {
        // Give
        Long ownerId = 33L;
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setId(ownerId);

        // Mocking behavior
        when(ownerServiceImp.findById(ownerId)).thenReturn(ownerResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/owner/findById/{id}", ownerId)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ownerResponse.getId()));

    }

    @Test
    void findAll() throws Exception {
        // Give
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setId(1L);
        OwnerResponse ownerResponse2 = new OwnerResponse();
        ownerResponse2.setId(2L);

        List<OwnerResponse> ownerResponseList = new ArrayList<>();
        ownerResponseList.add(ownerResponse);
        ownerResponseList.add(ownerResponse2);

        // Mocking behavior
        when(ownerServiceImp.findAll()).thenReturn(ownerResponseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/owner/findAll")
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(ownerResponseList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(ownerResponseList.get(1).getId()));

    }

    @Test
    void deleteById() throws Exception {
        // Give
        Long ownerId = 2L;

        // Mocking behavior
        when(ownerServiceImp.deleteById(ownerId)).thenReturn(true);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/owner/deleteById/{id}", ownerId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));

    }

    @Test
    void updateById() throws Exception {
        // Give
        Long ownerId = 44L;
        OwnerRequest ownerRequest = new OwnerRequest();

        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setId(ownerId);

        // Mocking behavior
        when(ownerServiceImp.updateById(ownerId, ownerRequest)).thenReturn(ownerResponse);

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/owner/updateById/{id}", ownerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ownerRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ownerResponse.getId()));

    }
}