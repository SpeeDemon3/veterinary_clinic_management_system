package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.OwnerRepository;
import com.aruiz.user.notification.repository.PetRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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
    void findById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateById() {
    }

    @Test
    void findByIdentificationCode() {
    }
}