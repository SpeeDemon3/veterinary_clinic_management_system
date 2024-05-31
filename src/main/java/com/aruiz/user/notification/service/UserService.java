package com.aruiz.user.notification.service;

import com.aruiz.user.notification.controller.dto.SignUpRequest;
import com.aruiz.user.notification.controller.dto.UserRequestUpdate;
import com.aruiz.user.notification.controller.dto.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsService {

    UserResponse save(SignUpRequest userRequest) throws Exception;

    List<UserResponse> findAll() throws Exception;

    UserResponse findById(Long id) throws Exception;

    String deleteById(Long id) throws Exception;

    UserResponse updateById(Long id, UserRequestUpdate userRequest) throws Exception;

    void addUserImg(Long id, MultipartFile imageFile) throws IOException;

    byte[] getUserImg(Long id) throws Exception;

    String usersInfoDownloadCsv() throws Exception;

    String usersInfoDownloadJson() throws Exception;

    UserDetails loadUserByUsername(String emailUser) throws UsernameNotFoundException;
}
