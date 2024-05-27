package com.aruiz.user.notification.service;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.entity.PetEntity;

import java.util.List;
import java.util.Optional;

public interface PetService {

    PetResponse save(PetRequest petRequest, Long ownerId) throws Exception;

    List<PetResponse> findAll() throws Exception;

    PetResponse findById(Long id) throws Exception;

    String deleteById(Long id) throws Exception;

    PetResponse updateById(Long id, PetRequestUpdate petRequestUpdate) throws Exception;

    Optional<PetEntity> findByCode(String identificationCode) throws Exception;

}
