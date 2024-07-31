package com.aruiz.user.notification.service.impl;

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

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {


    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerServiceImp ownerServiceImp;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ObjectMapper objectMapper;


    @Test
    void save() {
        // given

        // when

        //then

    }

    @Test
    void findAll() {
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