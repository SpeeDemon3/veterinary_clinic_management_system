package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.service.PetService;
import com.aruiz.user.notification.service.impl.PetServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PetServiceImpl petService;

    @Test
    void addPet() throws Exception {
        // Given
        Long veterinarianId = 3L;
        PetRequest petRequest = new PetRequest();
        petRequest.setName("Buddy");
        PetResponse petResponseMock = new PetResponse();
        petResponseMock.setId(1L);

        // Mocking behavior
        when(petService.save(veterinarianId, petRequest)).thenReturn(petResponseMock);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/pet/add/{veterinarianId}", veterinarianId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(petResponseMock.getId()));

    }

    @Test
    void findById() {
    }

    @Test
    void findAllPets() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateById() {
    }

    @Test
    void addPetImg() {
    }

    @Test
    void getPetImg() {
    }

    @Test
    void downloadFileCsvPets() {
    }

    @Test
    void findByCode() {
    }

    @Test
    void downloadFileJsonPets() {
    }
}