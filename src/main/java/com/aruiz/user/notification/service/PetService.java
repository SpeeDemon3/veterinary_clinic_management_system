package com.aruiz.user.notification.service;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.entity.PetEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PetService {

    PetResponse save(Long ownerId, PetRequest petRequest) throws Exception;

    List<PetResponse> findAll() throws Exception;

    PetResponse findById(Long id) throws Exception;

    String deleteById(Long id) throws Exception;

    PetResponse updateById(Long id, PetRequestUpdate petRequestUpdate) throws Exception;

    void addPetImg(Long id, MultipartFile imageFile) throws Exception;

    byte[] getPetImg(Long id) throws Exception;

    String petsInfoDownloadCsv() throws IOException;

    Optional<PetEntity> findByIdentificationCode(String identificationCode) throws Exception;

    String petsInfoDownloadJson() throws Exception;


}
