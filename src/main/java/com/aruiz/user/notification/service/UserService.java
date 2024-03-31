package com.aruiz.user.notification.service;

import com.aruiz.user.notification.controller.dto.UserRequest;
import com.aruiz.user.notification.controller.dto.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserResponse save(UserRequest userRequest) throws Exception;

    List<UserResponse> findAll() throws Exception;

    UserResponse findById(Long id) throws Exception;

    String deleteById(Long id) throws Exception;

    UserResponse updateById(Long id, UserRequest userRequest) throws Exception;

    UserDetails loadUserByUsername(String emailUser) throws UsernameNotFoundException;
}
