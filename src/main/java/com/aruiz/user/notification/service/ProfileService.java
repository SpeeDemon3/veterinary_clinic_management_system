package com.aruiz.user.notification.service;

import com.aruiz.user.notification.controller.dto.ProfileRequest;
import com.aruiz.user.notification.controller.dto.ProfileResponse;

import java.util.List;

public interface ProfileService {

    ProfileResponse save(ProfileRequest profileRequest, Long userId) throws Exception;

    ProfileResponse findById(Long id) throws Exception;

    List<ProfileResponse> findAll() throws Exception;

    String deleteByID(Long id) throws Exception;

    ProfileResponse updateById(Long id, ProfileRequest profileRequest) throws Exception;

}
