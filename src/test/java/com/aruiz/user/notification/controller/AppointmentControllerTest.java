package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.service.impl.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    void save() {
        // Give
        

        when(appointmentService.save()).thenReturn();

        ///addAppointment/{idVeterinarian}/{idPet}

    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAppointmentsByPetId() {
    }

    @Test
    void findAppointmentsByVeterinarianId() {
    }

    @Test
    void findAppointmentsByDate() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateById() {
    }
}