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

/**
 * Service interface for managing users, extending UserDetailsService.
 * Provides additional methods for user management beyond authentication.
 *
 * @author Antonio Ruiz
 */
public interface UserService extends UserDetailsService {

    UserResponse save(SignUpRequest userRequest) throws Exception;

    List<UserResponse> findAll() throws Exception;

    UserResponse findById(Long id) throws Exception;

    UserResponse findByDni (String dni) throws Exception;

    UserResponse findByEmail (String email) throws Exception;

    String deleteById(Long id) throws Exception;

    UserResponse updateById(Long id, UserRequestUpdate userRequest) throws Exception;

    void addUserImg(Long id, MultipartFile imageFile) throws IOException;

    byte[] getUserImg(Long id) throws Exception;

    String usersInfoDownloadCsv() throws Exception;

    String usersInfoDownloadJson() throws Exception;

    UserDetails loadUserByUsername(String emailUser) throws UsernameNotFoundException;
}
