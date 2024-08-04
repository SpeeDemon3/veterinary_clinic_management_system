package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.AppointmentRequest;
import com.aruiz.user.notification.controller.dto.AppointmentRequestUpdate;
import com.aruiz.user.notification.controller.dto.AppointmentResponse;
import com.aruiz.user.notification.domain.Pet;
import com.aruiz.user.notification.domain.User;
import com.aruiz.user.notification.entity.AppointmentEntity;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.AppointmentRepository;
import com.aruiz.user.notification.repository.PetRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.converter.AppointmentConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImpTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImp appointmentServiceImp;

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AppointmentConverter appointmentConverter;


    private final String[] HEADER = {"ID", "DATE OF APPOINTMENT", "DESCRIPTION", "VETERINARIAN", "PET"};

    // Give
    // Mocking behavior
    // When
    // Then

    @Test
    void save() throws Exception {
        // Give
        Long veterinarianId = 1L;
        Long petId = 1L;

        PetEntity petEntity = new PetEntity();
        petEntity.setId(petId);

        Pet pet = new Pet();
        pet.setId(petId);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(veterinarianId);

        User user = new User();
        user.setId(veterinarianId);

        AppointmentRequest appointmentRequest = new AppointmentRequest();

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setVeterinarian(userEntity);
        appointmentEntity.setPet(petEntity);

        AppointmentResponse appointmentResponseMock = new AppointmentResponse();
        appointmentResponseMock.setPet(pet);
        appointmentResponseMock.setVeterinarian(user);

        // Mocking behavior
        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(userRepository.findById(veterinarianId)).thenReturn(Optional.of(userEntity));
        when(appointmentConverter.toAppointmentEntity(appointmentRequest)).thenReturn(appointmentEntity);
        when(appointmentRepository.save(appointmentEntity)).thenReturn(appointmentEntity);
        when(appointmentConverter.toAppointmentResponse(appointmentEntity)).thenReturn(appointmentResponseMock);

        // When
        AppointmentResponse response = appointmentServiceImp.save(veterinarianId, petId, appointmentRequest);

        // Then
        assertEquals(appointmentResponseMock, response);
        verify(petRepository, times(1)).findById(petId);
        verify(userRepository, times(1)).findById(veterinarianId);
        verify(appointmentConverter, times(1)).toAppointmentEntity(appointmentRequest);
        verify(appointmentRepository, times(1)).save(appointmentEntity);
        verify(appointmentConverter, times(1)).toAppointmentResponse(appointmentEntity);
    }

    @Test
    void findAll() throws Exception {
        // Give
        List<AppointmentEntity> appointmentEntityList = new ArrayList<>();
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        AppointmentEntity appointmentEntity2 = new AppointmentEntity();
        appointmentEntityList.add(appointmentEntity);
        appointmentEntityList.add(appointmentEntity2);

        List<AppointmentResponse> appointmentResponseListMock = new ArrayList<>();
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        AppointmentResponse appointmentResponse2 = new AppointmentResponse();
        appointmentResponseListMock.add(appointmentResponse);
        appointmentResponseListMock.add(appointmentResponse2);

        // Mocking behavior
        when(appointmentRepository.findAll()).thenReturn(appointmentEntityList);
        when(appointmentConverter.toAppointmentResponse(appointmentEntityList.get(0))).thenReturn(new AppointmentResponse());

        // When
        List<AppointmentResponse> responses = appointmentServiceImp.findAll();

        // Then
        assertEquals(appointmentResponseListMock.size(), responses.size());
        assertEquals(appointmentResponseListMock.get(1), responses.get(1));

        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void findById() throws Exception {
        // Give
        Long id = 3L;
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(id);

        AppointmentResponse appointmentResponseMock = new AppointmentResponse();

        // Mocking behavior
        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointmentEntity));
        when(appointmentConverter.toAppointmentResponse(appointmentEntity)).thenReturn(appointmentResponseMock);

        // When
        AppointmentResponse response = appointmentServiceImp.findById(id);

        // Then
        assertEquals(appointmentResponseMock.getId(), response.getId());

        verify(appointmentRepository, times(1)).findById(id);

    }

    @Test
    void findAppointmentsByPetId() throws Exception {
        // Give
        Long petId = 5L;

        PetEntity petEntity = new PetEntity();
        petEntity.setId(petId);

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(1L);
        appointmentEntity.setPet(petEntity);
        AppointmentEntity appointmentEntity2 = new AppointmentEntity();
        appointmentEntity2.setId(2L);
        appointmentEntity2.setPet(petEntity);

        List<AppointmentEntity> appointmentEntityList = new ArrayList<>();
        appointmentEntityList.add(appointmentEntity);
        appointmentEntityList.add(appointmentEntity2);

        List<AppointmentResponse> appointmentResponseListMock = new ArrayList<>();
        appointmentResponseListMock.add(new AppointmentResponse());
        appointmentResponseListMock.add(new AppointmentResponse());

        // Mocking behavior
        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(appointmentRepository.findAll()).thenReturn(appointmentEntityList);
        when(appointmentConverter.toAppointmentResponse(appointmentEntity)).thenReturn(new AppointmentResponse());

        // When
        List<AppointmentResponse> responses = appointmentServiceImp.findAppointmentsByPetId(petId);

        // Then
        assertEquals(appointmentResponseListMock.size(), responses.size());
        assertEquals(appointmentResponseListMock.get(0), responses.get(0));

    }

    @Test
    void findAppointmentsByVeterinarianId() throws Exception {
        // Give
        Long veterinarianId = 33L;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(veterinarianId);

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setVeterinarian(userEntity);
        AppointmentEntity appointmentEntity2 = new AppointmentEntity();
        appointmentEntity2.setVeterinarian(userEntity);

        List<AppointmentEntity> appointmentEntityList = new ArrayList<>();
        appointmentEntityList.add(appointmentEntity);
        appointmentEntityList.add(appointmentEntity2);

        List<AppointmentResponse> appointmentResponseListMock = new ArrayList<>();
        appointmentResponseListMock.add(new AppointmentResponse());
        appointmentResponseListMock.add(new AppointmentResponse());

        // Mocking behavior
        when(appointmentRepository.findAll()).thenReturn(appointmentEntityList);
        when(userRepository.findById(veterinarianId)).thenReturn(Optional.of(userEntity));
        when(appointmentConverter.toAppointmentResponse(appointmentEntity)).thenReturn(new AppointmentResponse());

        // When
        List<AppointmentResponse> responses = appointmentServiceImp.findAppointmentsByVeterinarianId(veterinarianId);

        // Then
        assertEquals(appointmentResponseListMock.get(0), responses.get(0));

        verify(userRepository, times(1)).findById(veterinarianId);

    }

    @Test
    void findAppointmentsByDateOfAppointment() throws Exception {
        // Give
        String date = "03-08-2024";

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDateOfAppointment(date);
        AppointmentEntity appointmentEntity2 = new AppointmentEntity();
        appointmentEntity2.setDateOfAppointment(date);
        AppointmentEntity appointmentEntity3 = new AppointmentEntity();
        appointmentEntity3.setDateOfAppointment(date);

        List<AppointmentEntity> appointmentEntityList = new ArrayList<>();
        appointmentEntityList.add(appointmentEntity);
        appointmentEntityList.add(appointmentEntity2);
        appointmentEntityList.add(appointmentEntity3);

        List<AppointmentResponse> appointmentResponseListMock = new ArrayList<>();
        appointmentResponseListMock.add(new AppointmentResponse());
        appointmentResponseListMock.add(new AppointmentResponse());
        appointmentResponseListMock.add(new AppointmentResponse());

        // Mocking behavior
        when(appointmentRepository.findAppointmentsByDateOfAppointment(date)).thenReturn(appointmentEntityList);
        when(appointmentConverter.toAppointmentResponse(appointmentEntityList.get(1))).thenReturn(new AppointmentResponse());

        // When
        List<AppointmentResponse> responses = appointmentServiceImp.findAppointmentsByDateOfAppointment(date);

        // Then
        assertEquals(appointmentResponseListMock.get(2), responses.get(2));

    }

    @Test
    void deleteById() throws Exception {
        // Give
        Long id = 69L;
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(id);

        // Mocking behavior
        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointmentEntity));
        doNothing().when(appointmentRepository).deleteById(id);

        // When
        boolean response = appointmentServiceImp.deleteById(id);

        // Then
        assertEquals(true, response);
        verify(appointmentRepository, times(1)).deleteById(id);

    }

    @Test
    void updateById() throws Exception {
        // Give
        Long id = 3L;

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(id);

        AppointmentResponse appointmentResponseMock = new AppointmentResponse();

        // Mocking behavior
        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointmentEntity));
        when(appointmentRepository.save(appointmentEntity)).thenReturn(appointmentEntity);
        when(appointmentConverter.toAppointmentResponse(appointmentEntity)).thenReturn(appointmentResponseMock);

        // When
        AppointmentResponse response = appointmentServiceImp.updateById(id, new AppointmentRequestUpdate());

        // Then
        assertEquals(appointmentResponseMock.getId(), response.getId());
        assertEquals(appointmentResponseMock, response);

        verify(appointmentRepository, times(1)).save(appointmentEntity);

    }

}