package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.OwnerRequest;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.repository.OwnerRepository;
import com.aruiz.user.notification.service.OwnerService;
import com.aruiz.user.notification.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OwnerServiceImp implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public OwnerResponse save(Long petId, OwnerRequest ownerRequest) throws Exception {

        Optional<PetResponse> optionalPet = Optional.ofNullable(petService.findById(petId));

        if (ownerRequest != null && optionalPet != null) {

            List<PetEntity> petEntityList = new ArrayList<>();
            PetEntity petEntity = modelMapper.map(optionalPet.get(), PetEntity.class);
            petEntityList.add(petEntity);

            OwnerEntity ownerEntity = modelMapper.map(ownerRequest, OwnerEntity.class);

            ownerEntity.setPets(petEntityList);
            petEntity.setOwner(ownerEntity);

            petService.updateById(petId, modelMapper.map(petEntity, PetRequestUpdate.class));

            ownerRepository.save(ownerEntity);
            log.info("Owner created successfully!!! -> {}", ownerEntity.toString());

            return modelMapper.map(ownerEntity, OwnerResponse.class);
        }

        throw new Exception("Something went wrong!!!!");
    }

    @Override
    public List<OwnerResponse> findAll() throws Exception {
        return List.of();
    }

    @Override
    public OwnerResponse findById(Long id) throws Exception {
        return null;
    }

    @Override
    public String deleteById(Long id) throws Exception {
        return "";
    }

    @Override
    public OwnerResponse updateById(Long id, OwnerRequest ownerRequest) throws Exception {
        return null;
    }

    @Override
    public String ownerInfoDownloadCsv() throws Exception {
        return "";
    }

    @Override
    public String ownerInfoDownloadJson() throws Exception {
        return "";
    }

    @Override
    public OwnerResponse findByEmail(String email) throws Exception {

        Optional<OwnerEntity> optionalOwnerEntity = ownerRepository.findByEmail(email);

        if (optionalOwnerEntity.isPresent()) {
            OwnerResponse ownerResponse = modelMapper.map(optionalOwnerEntity.get(), OwnerResponse.class);
            log.info("Owner found with email -> {}", email);
            return ownerResponse;
        }

        throw new Exception("Owner not found with email -> " + email);
    }

    @Override
    public OwnerResponse findByDni(String dni) throws Exception {
        return null;
    }
}
