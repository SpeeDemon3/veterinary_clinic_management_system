package com.aruiz.user.notification.service;

import com.aruiz.user.notification.controller.dto.RoleRequest;
import com.aruiz.user.notification.controller.dto.RoleResponse;
import com.aruiz.user.notification.domain.Role;

import java.util.List;

public interface RoleService {

    RoleResponse save(RoleRequest roleRequest) throws Exception;

    RoleResponse findById(Long id) throws Exception;

    List<RoleResponse> findAll() throws Exception;

    RoleResponse updateById(Long id, RoleRequest roleRequest) throws Exception;

    Boolean deleteById(Long id) throws Exception;

    List<RoleResponse> findAllByName(String name) throws Exception;

    Role findByName(String nane) throws Exception;

}
