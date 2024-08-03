package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.PetRepository;
import com.aruiz.user.notification.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;


    @Test
    void save() throws Exception {
        // Given
        Long veterinarianId = 3L;
        PetRequest petRequest = new PetRequest();
        PetEntity petEntity = new PetEntity();
        PetResponse petResponse = new PetResponse();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(veterinarianId);
        petResponse.setVeterinarian(userEntity);

        // Mocking behavior
        when(userRepository.findById(veterinarianId)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(petRequest, PetEntity.class)).thenReturn(petEntity);
        when(petRepository.save(petEntity)).thenReturn(petEntity);
        when(modelMapper.map(petEntity, PetResponse.class)).thenReturn(petResponse);

        // When
        PetResponse response = petService.save(veterinarianId, petRequest);

        //then
        assertEquals(petResponse, response);

    }

    @Test
    void findAll() throws Exception {
        // Given
        List<PetEntity> petEntityList = new ArrayList<>();
        PetEntity petEntity = new PetEntity();
        petEntityList.add(petEntity);

        List<PetResponse> petResponseList = new ArrayList<>();
        petResponseList.add(new PetResponse());

        // Mocking behavior
        when(petRepository.findAll()).thenReturn(petEntityList);
        when(modelMapper.map(petEntity, PetResponse.class)).thenReturn(new PetResponse());

        // When
        List<PetResponse> petResponseListService = petService.findAll();

        // Then
        assertEquals(petResponseList.size(), petResponseListService.size());
        assertEquals(petResponseList.get(0).getId(), petResponseListService.get(0).getId());

    }

    @Test
    void findById() throws Exception {
        // Given
        Long petId = 2L;
        PetEntity petEntity = new PetEntity();
        petEntity.setId(petId);

        PetResponse petResponse = new PetResponse();
        petResponse.setId(petId);

        // Mocking behavior
        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(modelMapper.map(petEntity, PetResponse.class)).thenReturn(petResponse);

        // When
        PetResponse response = petService.findById(petId);

        // Then
        assertEquals(response, petResponse);
        verify(petRepository, times(1)).findById(petId);

    }

    @Test
    void deleteById() throws Exception {
        // Given
        Long petId = 1L;
        PetEntity petEntity = new PetEntity();
        petEntity.setId(petId);

        // Mocking behavior
        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        doNothing().when(petRepository).deleteById(petId);

        // When
        Boolean result = petService.deleteById(petId);

        // Then
        assertEquals(true, result);
        verify(petRepository, times(1)).deleteById(petId);

    }

    @Test
    void updateById() throws Exception {
        // Given
        Long petId = 5L;
        PetRequestUpdate petRequestUpdate = new PetRequestUpdate();
        PetEntity petEntity = new PetEntity();
        petEntity.setId(petId);
        PetResponse petResponse = new PetResponse();

        // Mocking behavior
        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(modelMapper.map(petRequestUpdate, PetEntity.class)).thenReturn(petEntity);
        when(petRepository.save(petEntity)).thenReturn(petEntity);
        when(modelMapper.map(petEntity, PetResponse.class)).thenReturn(petResponse);

        // When
        PetResponse response = petService.updateById(petId, petRequestUpdate);

        // Then
        assertEquals(petResponse, response);
        assertEquals(petResponse.getId(), response.getId());

        verify(petRepository, times(1)).save(petEntity);

    }

    @Test
    void findByIdentificationCode() throws Exception {
        // Give
        String code = "Test3";
        PetEntity petEntity = new PetEntity();
        petEntity.setIdentificationCode(code);

        PetResponse petResponseMock = new PetResponse();
        petResponseMock.setIdentificationCode(code);

        // Mocking behavior
        when(petRepository.findByIdentificationCode(code)).thenReturn(Optional.of(petEntity));
        when(modelMapper.map(petEntity, PetResponse.class)).thenReturn(petResponseMock);

        // When
        PetResponse response = petService.findByIdentificationCode(code);

        // Then
        assertEquals(petResponseMock, response);
        assertEquals(petResponseMock.getIdentificationCode(), response.getIdentificationCode());

        verify(petRepository, times(1)).findByIdentificationCode(code);

    }

}