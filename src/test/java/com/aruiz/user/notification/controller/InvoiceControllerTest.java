package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.InvoiceRequest;
import com.aruiz.user.notification.controller.dto.InvoiceResponse;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.domain.Owner;
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

@WebMvcTest(InvoiceController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad
class InvoiceControllerTest {

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

    @MockBean
    private OwnerServiceImp ownerServiceImp;

    @MockBean
    private InvoiceServiceImpl invoiceService;

    @Test
    void add() throws Exception {
        // Give
        String dniOwner = "33333333T";
        InvoiceRequest invoiceRequest = new InvoiceRequest();

        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(1L);

        // Mocking behavior
        when(invoiceService.save(dniOwner, invoiceRequest)).thenReturn(invoiceResponse);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/invoice/add/{dniOwner}", dniOwner)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(invoiceResponse.getId()));

    }

    @Test
    void findById() throws Exception {
        // Give
        Long id = 1L;

        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(id);

        // Mocking behavior
        when(invoiceService.findById(id)).thenReturn(invoiceResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/invoice/findById/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(invoiceResponse.getId()));

    }

    @Test
    void findByClientDni() throws Exception {
        // Give
        String dni = "33333333T";

        Owner owner = new Owner();
        owner.setId(1L);
        owner.setDni(dni);
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(3L);
        invoiceResponse.setClient(owner);

        List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
        invoiceResponseList.add(invoiceResponse);

        // Mocking behavior
        when(invoiceService.findByClientDni(dni)).thenReturn(invoiceResponseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/invoice/findByClientDni/{clientDni}", dni)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(invoiceResponse.getId()));

    }

    @Test
    void findByState() throws Exception {
        // Give
        String state = "paid";

        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(3L);
        invoiceResponse.setState(state);

        InvoiceResponse invoiceResponse2 = new InvoiceResponse();
        invoiceResponse2.setId(23L);
        invoiceResponse2.setState(state);

        List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
        invoiceResponseList.add(invoiceResponse);
        invoiceResponseList.add(invoiceResponse2);

        // Mocking behavior
        when(invoiceService.findByState(state)).thenReturn(invoiceResponseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/invoice/findByState/{state}", state)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(invoiceResponseList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(invoiceResponseList.get(1).getId()))
                .andExpect(jsonPath("$[0].state").value(invoiceResponseList.get(0).getState()))
                .andExpect(jsonPath("$[1].state").value(invoiceResponseList.get(1).getState()));

    }

    @Test
    void findAll() throws Exception {
        // Give
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(3L);
        InvoiceResponse invoiceResponse2 = new InvoiceResponse();
        invoiceResponse.setId(4L);

        List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
        invoiceResponseList.add(invoiceResponse);
        invoiceResponseList.add(invoiceResponse2);

        // Mocking behavior
        when(invoiceService.findAll()).thenReturn(invoiceResponseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/invoice/findAll")
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(invoiceResponseList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(invoiceResponseList.get(1).getId()));
    }

    @Test
    void updateById() throws Exception {
        // Give
        Long id = 23L;
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setInvoiceNumber("Test123");
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(id);
        invoiceResponse.setInvoiceNumber("Test321");

        // Mocking behavior
        when(invoiceService.updateById(id, invoiceRequest)).thenReturn(invoiceResponse);

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/invoice/updateById/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(invoiceResponse.getId()))
                .andExpect(jsonPath("$.invoiceNumber").value(invoiceResponse.getInvoiceNumber()));

    }

    @Test
    void deleteById() throws Exception {
        // Give
        Long id = 1L;

        // Mocking behavior
        when(invoiceService.deleteById(id)).thenReturn(true);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/invoice/deleteById/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));


    }
}