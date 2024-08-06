package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.RoleRequest;
import com.aruiz.user.notification.controller.dto.RoleResponse;
import com.aruiz.user.notification.service.impl.AuthenticationService;
import com.aruiz.user.notification.service.impl.JwtService;
import com.aruiz.user.notification.service.impl.RoleServicesImpl;
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

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad
class RoleControllerTest {

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
    private RoleServicesImpl roleServices;

    @Test
    void save() throws Exception {
        // Give
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName("ROLE_TEST");

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName("ROLE_TEST");

        // Mocking behavior
        when(roleServices.save(roleRequest)).thenReturn(roleResponse);

        // When
        ResultActions result = mockMvc.perform(post("/api/role/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleRequest))
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(roleResponse.getName()))
                .andExpect(jsonPath("$.id").value(roleResponse.getId()));

    }

    @Test
    void findById() throws Exception {
        // Give
        Long roleId = 3L;

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(roleId);
        roleResponse.setDescription("Testing....");

        // Mocking behavior
        when(roleServices.findById(roleId)).thenReturn(roleResponse);

        // When
        ResultActions result = mockMvc.perform(get("/api/role/findById/{id}", roleId)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(roleResponse.getId()))
                .andExpect(jsonPath("$.description").value(roleResponse.getDescription()));

    }

    @Test
    void findAll() throws Exception {
        // Give
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(2L);
        RoleResponse roleResponse2 = new RoleResponse();
        roleResponse2.setId(2L);

        List<RoleResponse> roleResponseList = new ArrayList<>();
        roleResponseList.add(roleResponse);
        roleResponseList.add(roleResponse2);

        // Mocking behavior
        when(roleServices.findAll()).thenReturn(roleResponseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/role/findAll")
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(roleResponseList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(roleResponseList.get(1).getId()));

    }

    @Test
    void updateById() throws Exception {
        // Given
        Long id = 5L;
        RoleRequest roleRequest = new RoleRequest();

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(id);

        // Mocking behavior
        when(roleServices.updateById(id, roleRequest)).thenReturn(roleResponse);

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/role/updateById/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(roleResponse.getId()));

    }

    @Test
    void deleteById() throws Exception {
        // Give
        Long roleId = 1L;

        // Mocking behavior
        when(roleServices.deleteById(roleId)).thenReturn(true);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/role/deleteById/{id}", roleId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));

    }
}