package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.OwnerRequest;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.OwnerRepository;
import com.aruiz.user.notification.service.converter.OwnerConverter;
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
class OwnerServiceImpTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerServiceImp ownerServiceImp;

    @Mock
    private PetServiceImpl petService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private OwnerConverter ownerConverter;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void save() throws Exception {
        // Given
        Long petId = 3L;
        PetResponse petResponse = new PetResponse();
        petResponse.setId(petId);

        PetEntity petEntity = new PetEntity();
        petEntity.setId(petId);

        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setDni("11111111T");

        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setId(1L);
        ownerEntity.setDni("11111111T");

        OwnerResponse ownerResponseMock = new OwnerResponse();
        ownerResponseMock.setId(1L);
        ownerResponseMock.setDni("11111111T");

        PetRequestUpdate petRequestUpdate = new PetRequestUpdate();

        // Mocking behavior
        when(petService.findById(petId)).thenReturn(petResponse);
        when(modelMapper.map(petResponse, PetEntity.class)).thenReturn(petEntity);
        when(modelMapper.map(ownerRequest, OwnerEntity.class)).thenReturn(ownerEntity);
        when(ownerRepository.save(ownerEntity)).thenReturn(ownerEntity);
        when(modelMapper.map(petEntity, PetRequestUpdate.class)).thenReturn(petRequestUpdate);
        when(petService.updateById(petId, petRequestUpdate)).thenReturn(petResponse);
        when(modelMapper.map(ownerEntity, OwnerResponse.class)).thenReturn(ownerResponseMock);

        // When
        OwnerResponse response = ownerServiceImp.save(petId, ownerRequest);

        // Then
        assertEquals(ownerResponseMock, response);
        verify(petService, times(1)).findById(petId);
        verify(modelMapper, times(1)).map(petResponse, PetEntity.class);
        verify(modelMapper, times(1)).map(ownerRequest, OwnerEntity.class);
        verify(ownerRepository, times(1)).save(ownerEntity);
        verify(modelMapper, times(1)).map(petEntity, PetRequestUpdate.class);
        verify(petService, times(1)).updateById(petId, petRequestUpdate);
        verify(modelMapper, times(1)).map(ownerEntity, OwnerResponse.class);
    }



    @Test
    void findAll() throws Exception {
        // Give
        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setId(1L);
        OwnerEntity ownerEntity2 = new OwnerEntity();
        ownerEntity2.setId(2L);

        List<OwnerEntity> ownerEntityList = new ArrayList<>();
        ownerEntityList.add(ownerEntity);
        ownerEntityList.add(ownerEntity2);

        List<OwnerResponse> ownerResponseListMock = new ArrayList<>();
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setId(1L);
        OwnerResponse ownerResponse1 = new OwnerResponse();
        ownerResponseListMock.add(ownerResponse);
        ownerResponseListMock.add(ownerResponse1);

        // Mocking behavior
        when(ownerRepository.findAll()).thenReturn(ownerEntityList);
        when(ownerConverter.toOwnerResponse(ownerEntityList.get(0))).thenReturn(ownerResponseListMock.get(0));

        // When
        List<OwnerResponse> responses = ownerServiceImp.findAll();

        // Then
        assertEquals(ownerResponseListMock.size(), responses.size());

    }

    @Test
    void findById() throws Exception {
        // Give
        Long ownerId = 3L;
        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setId(ownerId);

        OwnerResponse ownerResponseMock = new OwnerResponse();
        ownerResponseMock.setId(ownerId);

        // Mocking behavior
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(ownerEntity));
        when(ownerConverter.toOwnerResponse(ownerEntity)).thenReturn(ownerResponseMock);

        // When
        OwnerResponse response = ownerServiceImp.findById(ownerId);

        // Then
        assertEquals(ownerResponseMock, response);

        verify(ownerRepository, times(1)).findById(ownerId);

    }

    @Test
    void deleteById() throws Exception  {
        // Give
        Long ownerId = 3L;
        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setId(ownerId);

        // Mocking behavior
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(ownerEntity));

        // When
        Boolean response = ownerServiceImp.deleteById(ownerId);

        // Then
        assertEquals(true, response);

        verify(ownerRepository, times(1)).deleteById(ownerId);

    }

    @Test
    void deleteById_nonExistingId_throwsException() {
        // Given
        Long ownerId = 1L;

        // Mocking behavior
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> ownerServiceImp.deleteById(ownerId));
        assertNotNull(exception);
        verify(ownerRepository, times(0)).deleteById(ownerId);
    }

    @Test
    void updateById() throws Exception {
        // Give
        Long ownerId = 3L;
        OwnerRequest ownerRequest = new OwnerRequest();

        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setId(ownerId);

        OwnerResponse ownerResponseMock = new OwnerResponse();

        // Mocking behavior
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(ownerEntity));
        when(ownerRepository.save(ownerEntity)).thenReturn(ownerEntity);
        when(modelMapper.map(ownerEntity, OwnerResponse.class)).thenReturn(ownerResponseMock);

        // When
        OwnerResponse response = ownerServiceImp.updateById(ownerId, ownerRequest);

        // Then
        assertEquals(ownerResponseMock, response);
        assertEquals(ownerResponseMock.getId(), response.getId());

        verify(ownerRepository, times(1)).save(ownerEntity);

    }

    @Test
    void findByEmail() throws Exception {
        // Give
        String email = "test@test.es";
        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setEmail(email);

        OwnerResponse ownerResponseMock = new OwnerResponse();

        // Mocking behavior
        when(ownerRepository.findByEmail(email)).thenReturn(Optional.of(ownerEntity));
        when(modelMapper.map(ownerEntity, OwnerResponse.class)).thenReturn(ownerResponseMock);

        // When
        OwnerResponse response = ownerServiceImp.findByEmail(email);

        assertEquals(ownerResponseMock, response);
        assertEquals(ownerResponseMock.getEmail(), response.getEmail());

        verify(ownerRepository, times(1)).findByEmail(email);

    }

    @Test
    void findByDni() throws Exception {
        // Give
        String dni = "33333333T";
        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setDni(dni);

        OwnerResponse ownerResponseMock = new OwnerResponse();

        // Mocking behavior
        when(ownerRepository.findByDni(dni)).thenReturn(Optional.of(ownerEntity));
        when(ownerConverter.toOwnerResponse(ownerEntity)).thenReturn(ownerResponseMock);

        // When
        OwnerResponse response = ownerServiceImp.findByDni(dni);

        // Then
        assertEquals(ownerResponseMock, response);
        assertEquals(ownerResponseMock.getId(), response.getId());
        assertEquals(ownerResponseMock.getDni(), response.getDni());

        verify(ownerRepository, times(1)).findByDni(dni);

    }
}