package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.InvoiceResponse;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.entity.InvoiceEntity;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.repository.InvoiceRepository;
import com.aruiz.user.notification.service.converter.AppointmentConverter;
import com.aruiz.user.notification.service.converter.InvoiceConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
    private InvoiceConverter invoiceConverter;

    // Give
    // Mocking behavior
    // When
    // Then

    @Test
    void save() {
        // Give
        // Mocking behavior
        // When
        // Then
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
    void updateById() {
        // Give
        // Mocking behavior
        // When
        // Then
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
    void invoicesInfoDownloadCsv() {
    }

    @Test
    void invoicesInfoDownloadJson() {
    }
}