package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.repository.OwnerRepository;
import com.aruiz.user.notification.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OwnerServiceImp {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private ModelMapper modelMapper;

    


}
