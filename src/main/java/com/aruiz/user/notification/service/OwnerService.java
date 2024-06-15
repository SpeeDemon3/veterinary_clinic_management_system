package com.aruiz.user.notification.service;


import com.aruiz.user.notification.controller.dto.OwnerRequest;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.entity.OwnerEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing owners.
 *
 * @author Antonio Ruiz
 */
public interface OwnerService {

    OwnerResponse save (Long petId, OwnerRequest ownerRequest) throws Exception;
    List<OwnerResponse> findAll () throws Exception;
    OwnerResponse findById (Long id) throws Exception;
    String deleteById (Long id) throws Exception;
    OwnerResponse updateById (Long id, OwnerRequest ownerRequest) throws Exception;
    String ownerInfoDownloadCsv () throws Exception;
    String ownerInfoDownloadJson () throws Exception;

    OwnerResponse findByEmail (String email) throws Exception;
    OwnerResponse findByDni (String dni) throws Exception;
}
