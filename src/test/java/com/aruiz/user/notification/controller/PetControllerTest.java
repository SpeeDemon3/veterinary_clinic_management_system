package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.service.impl.AuthenticationService;
import com.aruiz.user.notification.service.impl.JwtService;
import com.aruiz.user.notification.service.impl.PetServiceImpl;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PetController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetServiceImpl petService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addPet() throws Exception {
        // Give
        Long veterinarianId = 3L;
        PetRequest petRequest = new PetRequest();
        petRequest.setName("testPet");
        PetResponse petResponse = new PetResponse();
        petResponse.setName("test");

        // Mocking behavior
        when(petService.save(veterinarianId, petRequest)).thenReturn(petResponse);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/pet/add/{veterinarianId}", veterinarianId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(petResponse.getName()));
    }

    @Test
    void findById() throws Exception {
        // Give
        Long petId = 23L;
        PetResponse petResponse = new PetResponse();
        petResponse.setId(petId);
        petResponse.setName("Test");

        // Mocking behavior
        when(petService.findById(petId)).thenReturn(petResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/pet/findById/{id}", petId)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(petId))
                .andExpect(jsonPath("$.name").value("Test"));

    }

    @Test
    void findAllPets() throws Exception {
        // Give
        PetResponse petResponse = new PetResponse();
        petResponse.setId(3L);

        PetResponse petResponse2 = new PetResponse();
        petResponse2.setId(4L);

        List<PetResponse> petResponseList = new ArrayList<>();
        petResponseList.add(petResponse);
        petResponseList.add(petResponse2);

        // Mocking behavior
        when(petService.findAll()).thenReturn(petResponseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/pet/findAll")
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3L))
                .andExpect(jsonPath("$[1].id").value(4L));

    }

    @Test
    void deleteById() throws Exception {
        // Give
        Long petId = 2L;

        // Mocking behavior
        when(petService.deleteById(petId)).thenReturn(true);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/pet/deleteById/{id}", petId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));

    }

    @Test
    void updateById() throws Exception {
        // Given
        Long id = 3L;
        PetRequestUpdate petRequestUpdate = new PetRequestUpdate();
        petRequestUpdate.setName("Name");

        PetResponse petResponse = new PetResponse();
        petResponse.setId(id);
        petResponse.setName("Update name");

        // Mocking behavior
        when(petService.updateById(id, petRequestUpdate)).thenReturn(petResponse);

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/pet/updateById/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequestUpdate))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Update name"));

    }

    @Test
    void findByCode() throws Exception {
        // Give
        String code = "Test3A";
        PetResponse petResponse = new PetResponse();
        petResponse.setIdentificationCode(code);

        // Mocking behavior
        when(petService.findByIdentificationCode(code)).thenReturn(petResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/pet/findByCode/{code}", code)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.identificationCode").value(code));

    }
}