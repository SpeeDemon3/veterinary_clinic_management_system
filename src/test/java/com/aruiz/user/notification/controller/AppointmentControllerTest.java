package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.AppointmentRequest;
import com.aruiz.user.notification.controller.dto.AppointmentRequestUpdate;
import com.aruiz.user.notification.controller.dto.AppointmentResponse;
import com.aruiz.user.notification.service.impl.*;
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

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad
class AppointmentControllerTest {

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

    @MockBean
    private AppointmentServiceImp appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void save() throws Exception {
        // Give
        Long veterinarianId = 1L;
        Long petId= 3L;

        AppointmentRequest appointmentRequest = new AppointmentRequest();

        AppointmentResponse appointmentResponse = new AppointmentResponse();

        // Mocking behavior
        when(appointmentService.save(veterinarianId, petId, appointmentRequest)).thenReturn(appointmentResponse);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/appointment/addAppointment/{idVeterinarian}/{idPet}", veterinarianId, petId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(appointmentResponse.getId()));

    }

    @Test
    void findAll() throws Exception {
        // Give
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setId(1L);
        AppointmentResponse appointmentResponse2 = new AppointmentResponse();
        appointmentResponse2.setId(12L);

        List<AppointmentResponse> appointmentResponses = new ArrayList<>();
        appointmentResponses.add(appointmentResponse);
        appointmentResponses.add(appointmentResponse2);

        // Mocking behavior
        when(appointmentService.findAll()).thenReturn(appointmentResponses);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/appointment/findAll")
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(appointmentResponses.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(appointmentResponses.get(1).getId()));

    }

    @Test
    void findById() throws Exception {
        // Give
        Long id = 21L;
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setId(id);

        // Mocking behavior
        when(appointmentService.findById(id)).thenReturn(appointmentResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/appointment/findById/{idAppointment}", id)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(appointmentResponse.getId()));

    }

    @Test
    void findAppointmentsByPetId() throws Exception {
        // Give
        Long petId = 81L;
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setId(2L);
        AppointmentResponse appointmentResponse2 = new AppointmentResponse();
        appointmentResponse.setId(32L);

        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();
        appointmentResponseList.add(appointmentResponse);
        appointmentResponseList.add(appointmentResponse2);

        // Mocking behavior
        when(appointmentService.findAppointmentsByPetId(petId)).thenReturn(appointmentResponseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/appointment/findAppointmentsByPetId/{idAppointment}", petId)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(appointmentResponseList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(appointmentResponseList.get(1).getId()));

    }

    @Test
    void findAppointmentsByVeterinarianId() throws Exception {
        // Give
        Long veterinarianId = 81L;
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setId(2L);
        AppointmentResponse appointmentResponse2 = new AppointmentResponse();
        appointmentResponse.setId(32L);

        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();
        appointmentResponseList.add(appointmentResponse);
        appointmentResponseList.add(appointmentResponse2);

        // Mocking behavior
        when(appointmentService.findAppointmentsByVeterinarianId(veterinarianId)).thenReturn(appointmentResponseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/appointment/findAppointmentsByVeterinarianId/{id}", veterinarianId)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(appointmentResponseList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(appointmentResponseList.get(1).getId()));
    }

    @Test
    void findAppointmentsByDate() throws Exception {
        // Give
        String date = "12-12-2012";
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setId(2L);
        AppointmentResponse appointmentResponse2 = new AppointmentResponse();
        appointmentResponse.setId(32L);
        appointmentResponse2.setDateOfAppointment(date);

        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();
        appointmentResponseList.add(appointmentResponse);
        appointmentResponseList.add(appointmentResponse2);

        // Mocking behavior
        when(appointmentService.findAppointmentsByDateOfAppointment(date)).thenReturn(appointmentResponseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/appointment/findAppointmentsByDate/{dateOfAppointment}", date)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(appointmentResponseList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(appointmentResponseList.get(1).getId()))
                .andExpect(jsonPath("$[1].dateOfAppointment").value(appointmentResponseList.get(1).getDateOfAppointment()));
    }

    @Test
    void deleteById() throws Exception {
        // Give
        Long id = 33L;

        // Mocking behavior
        when(appointmentService.deleteById(id)).thenReturn(true);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/appointment/deleteById/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));

    }

    @Test
    void updateById() throws Exception {
        // Give
        Long id = 3L;
        AppointmentRequestUpdate appointmentRequestUpdate = new AppointmentRequestUpdate();
        appointmentRequestUpdate.setDescription("Test Request");
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setId(id);
        appointmentResponse.setDescription("Test Update");

        // Mocking behavior
        when(appointmentService.updateById(id, appointmentRequestUpdate)).thenReturn(appointmentResponse);

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/appointment/updateById/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentRequestUpdate))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(appointmentResponse.getId()))
                .andExpect(jsonPath("$.description").value(appointmentResponse.getDescription()));

    }
}