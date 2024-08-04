package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.InvoiceRequest;
import com.aruiz.user.notification.controller.dto.InvoiceResponse;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.domain.Owner;
import com.aruiz.user.notification.entity.InvoiceEntity;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.repository.InvoiceRepository;
import com.aruiz.user.notification.service.converter.InvoiceConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private OwnerServiceImp ownerServiceImp;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private InvoiceConverter invoiceConverter;

    private final String[] HEADERS = {"ID", "DATE OF ISSUE", "INVOICE NUMBER", "STATE", "TOTAL PRICE", "CLIENT ID"};


    @Test
    void save() throws Exception {
        // Give
        String clientDni = "22222222F";
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setDni(clientDni);

        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setDni(clientDni);

        Owner owner = new Owner();
        owner.setDni(clientDni);

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setClient(ownerEntity);

        InvoiceResponse invoiceResponseMock = new InvoiceResponse();
        invoiceResponseMock.setClient(owner);

        // Mocking behavior
        when(ownerServiceImp.findByDni(clientDni)).thenReturn(ownerResponse);
        when(modelMapper.map(invoiceRequest, InvoiceEntity.class)).thenReturn(invoiceEntity);
        when(modelMapper.map(ownerResponse, OwnerEntity.class)).thenReturn(new OwnerEntity());
        when(invoiceRepository.save(invoiceEntity)).thenReturn(invoiceEntity);
        when(modelMapper.map(invoiceEntity, InvoiceResponse.class)).thenReturn(invoiceResponseMock);

        // When
        InvoiceResponse invoiceResponse = invoiceService.save(clientDni, invoiceRequest);

        // Then
        assertEquals(invoiceResponseMock.getClient(), invoiceResponse.getClient());

    }

    @Test
    void findById() throws Exception {
        // Give
        Long id = 2L;
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(id);

        InvoiceResponse invoiceResponseMock = new InvoiceResponse();
        invoiceResponseMock.setId(id);

        // Mocking behavior
        when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoiceEntity));
        when(invoiceConverter.toInvoiceResponse(invoiceEntity)).thenReturn(invoiceResponseMock);

        // When
        InvoiceResponse response = invoiceService.findById(id);

        // Then
        assertEquals(invoiceResponseMock.getId(), response.getId());
        assertEquals(invoiceResponseMock, response);

        verify(invoiceRepository, times(1)).findById(id);

    }

    @Test
    void findByClientDni() throws Exception {
        // Give
        String dni = "33333333T";

        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setDni(dni);
        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setDni(dni);

        List<InvoiceEntity> invoiceEntityList = new ArrayList<>();
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setClient(ownerEntity);
        invoiceEntityList.add(invoiceEntity);

        List<InvoiceResponse> invoiceResponseListMock = new ArrayList<>();
        InvoiceResponse invoiceResponseMock = new InvoiceResponse();
        invoiceResponseListMock.add(invoiceResponseMock);

        // Mocking behavior
        when(ownerServiceImp.findByDni(dni)).thenReturn(ownerResponse);
        when(invoiceRepository.findByClientDni(dni)).thenReturn(Optional.of(invoiceEntityList));
        when(invoiceConverter.toInvoiceResponse(invoiceEntityList.get(0))).thenReturn(invoiceResponseListMock.get(0));
        // When
        List<InvoiceResponse> responses = invoiceService.findByClientDni(dni);

        // Then
        assertEquals(invoiceResponseListMock.size(), responses.size());
        assertEquals(invoiceResponseListMock.get(0), responses.get(0));

        verify(ownerServiceImp, times(1)).findByDni(dni);
        verify(invoiceRepository, times(1)).findByClientDni(dni);

    }

    @Test
    void findByState() throws Exception {
        // Give
        String state = "paid";

        List<InvoiceEntity> invoiceEntityList = new ArrayList<>();
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setState(state);
        invoiceEntityList.add(invoiceEntity);

        List<InvoiceResponse> invoiceResponseListMock = new ArrayList<>();
        InvoiceResponse invoiceResponseMock = new InvoiceResponse();
        invoiceResponseMock.setState(state);
        invoiceResponseListMock.add(invoiceResponseMock);

        // Mocking behavior
        when(invoiceRepository.findAll()).thenReturn(invoiceEntityList);
        when(modelMapper.map(invoiceEntity, InvoiceResponse.class)).thenReturn(invoiceResponseMock);

        // When
        List<InvoiceResponse> responses = invoiceService.findByState(state);

        // Then
        assertEquals(invoiceResponseListMock.get(0), responses.get(0));
        assertEquals(invoiceResponseListMock.get(0).getState(), responses.get(0).getState());

        verify(invoiceRepository, times(1)).findAll();

    }

    @Test
    void findAll() throws Exception {
        // Give

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        InvoiceEntity invoiceEntity1 = new InvoiceEntity();
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        InvoiceResponse invoiceResponse1 = new InvoiceResponse();

        List<InvoiceEntity> invoiceEntityList = new ArrayList<>();
        invoiceEntityList.add(invoiceEntity);
        invoiceEntityList.add(invoiceEntity1);

        List<InvoiceResponse> invoiceResponseListMock = new ArrayList<>();
        invoiceResponseListMock.add(invoiceResponse);
        invoiceResponseListMock.add(invoiceResponse1);

        // Mocking behavior
        when(invoiceRepository.findAll()).thenReturn(invoiceEntityList);
        when(modelMapper.map(invoiceEntityList.get(0), InvoiceResponse.class)).thenReturn(invoiceResponseListMock.get(0));

        // When
        List<InvoiceResponse> invoiceResponseList = invoiceService.findAll();

        // Then
        assertEquals(invoiceResponseListMock.size(), invoiceResponseList.size());
        assertEquals(invoiceResponseListMock.get(0), invoiceResponseList.get(0));

        verify(invoiceRepository, times(1)).findAll();

    }

    @Test
    void updateById() throws Exception {
        // Give
        Long invoiceId = 3L;

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(invoiceId);

        InvoiceRequest invoiceRequest = new InvoiceRequest();

        InvoiceResponse invoiceResponseMock = new InvoiceResponse();

        // Mocking behavior
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoiceEntity));
        when(invoiceRepository.save(invoiceEntity)).thenReturn(invoiceEntity);
        when(modelMapper.map(invoiceEntity, InvoiceResponse.class)).thenReturn(invoiceResponseMock);

        // When
        InvoiceResponse response = invoiceService.updateById(invoiceId, invoiceRequest);

        // Then
        assertEquals(invoiceResponseMock, response);
        assertEquals(invoiceResponseMock.getId(), response.getId());

        verify(invoiceRepository, times(1)).findById(invoiceId);
        verify(invoiceRepository, times(1)).save(invoiceEntity);

    }

    @Test
    void deleteById() throws Exception {
        // Give
        Long id = 1L;
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(id);

        // Mocking behavior
        when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoiceEntity));
        doNothing().when(invoiceRepository).deleteById(id);

        // When
        Boolean result = invoiceService.deleteById(id);

        // Then
        assertTrue(true);
        assertEquals(true, result);

        verify(invoiceRepository, times(1)).findById(id);
        verify(invoiceRepository, times(1)).deleteById(id);

    }

    @Test
    void invoicesInfoDownloadCsv() throws Exception {
        // Give
        OwnerEntity owner = new OwnerEntity();

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(1L);
        invoiceEntity.setInvoiceNumber("INV001");
        invoiceEntity.setTotalPrice(100.0);
        invoiceEntity.setClient(owner);
        invoiceEntity.setDateOfIssue(LocalDate.now());
        invoiceEntity.setState("paid");

        List<InvoiceEntity> invoiceEntityList = new ArrayList<>();
        invoiceEntityList.add(invoiceEntity);

        // Given
        when(invoiceRepository.findAll()).thenReturn(invoiceEntityList);

        // When
        String csvContent = invoiceService.invoicesInfoDownloadCsv();

        // Then
        StringBuilder expectedCsvContent = new StringBuilder();
        int count = 1;
        for (String header : HEADERS) {
            expectedCsvContent.append(header).append(",");
            if (count == HEADERS.length) {
                expectedCsvContent.append(header).append("\n");
            }
            count++;
        }
        expectedCsvContent
                .append(invoiceEntity.getId()).append(",")
                .append(invoiceEntity.getInvoiceNumber()).append(",")
                .append(invoiceEntity.getTotalPrice()).append(",")
                .append(invoiceEntity.getClient()).append(",")
                .append(invoiceEntity.getDateOfIssue()).append(",")
                .append(invoiceEntity.getState()).append("\n");

        assertEquals(expectedCsvContent.length(), csvContent.length());
        verify(invoiceRepository, times(1)).findAll();


    }

    @Test
    void invoicesInfoDownloadJson() throws JsonProcessingException {
        // Give
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(1L);
        invoiceEntity.setState("paid");

        List<InvoiceEntity> invoiceEntityList = new ArrayList<>();
        invoiceEntityList.add(invoiceEntity);

        String invoicesJson = "[{\"id\":1,\"state\":\"paid\"}]";

        // Mocking behavior
        when(objectMapper.writeValueAsString(invoiceEntityList)).thenReturn(invoicesJson);

        // When
        String respone = objectMapper.writeValueAsString(invoiceEntityList);

        // Then
        assertEquals(invoicesJson, respone);
    }

}