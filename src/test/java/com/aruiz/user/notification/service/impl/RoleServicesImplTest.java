package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.RoleRequest;
import com.aruiz.user.notification.controller.dto.RoleResponse;
import com.aruiz.user.notification.domain.Role;
import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.repository.PetRepository;
import com.aruiz.user.notification.repository.RoleRepository;
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

@ExtendWith(MockitoExtension.class)
class RoleServicesImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServicesImpl roleServices;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void save() throws Exception {
        // Given
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName("ROLE_TEST");
        RoleEntity roleEntity = new RoleEntity();
        RoleResponse roleResponseMock = new RoleResponse();

        // Mocking behavior
        when(modelMapper.map(roleRequest, RoleEntity.class)).thenReturn(roleEntity);
        when(roleRepository.save(roleEntity)).thenReturn(roleEntity);
        when(modelMapper.map(roleEntity, RoleResponse.class)).thenReturn(roleResponseMock);

        // When
        RoleResponse response = roleServices.save(roleRequest);

        // Then
        assertEquals(roleResponseMock, response);


    }

    @Test
    void findById() throws Exception {
        // Give
        Long roleId = 9L;
        RoleEntity roleEntity = new RoleEntity();
        RoleResponse roleResponseMock = new RoleResponse();

        // Mocking behavior
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleEntity));
        when(modelMapper.map(roleEntity, RoleResponse.class)).thenReturn(roleResponseMock);

        // When
        RoleResponse roleResponse = roleServices.findById(roleId);

        // Then
        assertEquals(roleResponseMock, roleResponse);

        verify(roleRepository, times(1)).findById(roleId);

    }

    @Test
    void findAll() throws Exception {
        // Give
        List<RoleEntity> roleEntityList = new ArrayList<>();
        RoleEntity roleEntity = new RoleEntity();
        roleEntityList.add(roleEntity);

        List<RoleResponse> roleResponseMockList = new ArrayList<>();
        RoleResponse roleResponseMock = new RoleResponse();
        roleResponseMockList.add(roleResponseMock);

        // Mocking behavior
        when(roleRepository.findAll()).thenReturn(roleEntityList);
        when(modelMapper.map(roleEntityList.get(0), RoleResponse.class)).thenReturn(roleResponseMockList.get(0));

        // When
        List<RoleResponse> responseList = roleServices.findAll();

        // Then
        assertEquals(roleResponseMockList.size(), responseList.size());
        assertEquals(roleResponseMockList.get(0).getId(), roleResponseMockList.get(0).getId());
    }

    @Test
    void updateById() throws Exception {
        // Give
        Long idRole = 2L;
        RoleRequest roleRequestUpdate = new RoleRequest();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(idRole);
        RoleResponse roleResponseMock = new RoleResponse();
        roleResponseMock.setId(idRole);

        // Mocking behavior
        when(roleRepository.findById(idRole)).thenReturn(Optional.of(roleEntity));
        when(roleRepository.save(roleEntity)).thenReturn(roleEntity);
        when(modelMapper.map(roleEntity, RoleResponse.class)).thenReturn(roleResponseMock);

        // When
        RoleResponse response = roleServices.updateById(idRole, roleRequestUpdate);

        // Then
        assertEquals(roleResponseMock, response);
        assertEquals(roleResponseMock.getId(), response.getId());

        verify(roleRepository, times(1)).save(roleEntity);

    }

    @Test
    void deleteById() throws Exception {
        // Give
        Long roleId = 3L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(roleId);

        // Mocking behavior
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleEntity));
        doNothing().when(roleRepository).deleteById(roleId);

        // When
        Boolean response = roleServices.deleteById(roleId);

        // Then
        assertEquals(true, response);

        verify(roleRepository, times(1)).deleteById(roleId);

    }

    @Test
    void findAllByName() throws Exception {
        // Give
        String name = "Test";
        RoleEntity roleEntity = new RoleEntity();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity.setName(name);
        roleEntity1.setName(name);

        List<RoleEntity> roleEntityList = new ArrayList<>();
        roleEntityList.add(roleEntity);
        roleEntityList.add(roleEntity1);

        List<RoleResponse> roleResponseMockList = new ArrayList<>();
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName(name);
        roleResponseMockList.add(roleResponse);
        RoleResponse roleResponse1 = new RoleResponse();
        roleResponse1.setName(name);
        roleResponseMockList.add(roleResponse1);

        // Mocking behavior
        when(roleRepository.findAllByName(name)).thenReturn(Optional.of(roleEntityList));
        when(modelMapper.map(roleEntityList.get(0), RoleResponse.class)).thenReturn(roleResponse);

        // When
        List<RoleResponse> responseList = roleServices.findAllByName(name);

        // Then
        assertEquals(roleResponseMockList.size(), responseList.size());
        assertEquals(roleResponseMockList.get(0).getName(), responseList.get(0).getName());
        assertEquals(roleResponseMockList.get(1).getName(), responseList.get(1).getName());

        verify(roleRepository, times(1)).findAllByName(name);

    }

    @Test
    void findByName() throws Exception {
        // Give
        String name = "Test";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);

        Role roleMock = new Role();
        roleMock.setName(name);

        // Mocking behavior
        when(roleRepository.findByName(name)).thenReturn(Optional.of(roleEntity));
        when(modelMapper.map(roleEntity, Role.class)).thenReturn(roleMock);

        // When
        Role response = roleServices.findByName(name);

        // Then
        assertEquals(roleMock.getName(), response.getName());

        verify(roleRepository, times(1)).findByName(name);

    }
}