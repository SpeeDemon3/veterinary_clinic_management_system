package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.NotificationRequest;
import com.aruiz.user.notification.controller.dto.NotificationResponse;
import com.aruiz.user.notification.controller.dto.UserResponse;
import com.aruiz.user.notification.entity.NotificationEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.NotificationRepository;
import com.aruiz.user.notification.service.NotificationService;
import com.aruiz.user.notification.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Override
    public NotificationResponse save(NotificationRequest notificationRequest, Long destinationUserId) throws Exception {

        if (destinationUserId > 0) {

            if (notificationRequest.getContent() != null) {

                UserResponse userResponse = userService.findById(destinationUserId);

                UserEntity userEntity = modelMapper.map(userResponse, UserEntity.class);

                notificationRequest.setDestinationUserId(destinationUserId);

                NotificationEntity notificationEntity = modelMapper.map(notificationRequest, NotificationEntity.class);

                return modelMapper.map(notificationEntity, NotificationResponse.class);

            }

        } else {
            throw new RuntimeException();
        }

        return null;
    }

    @Override
    public NotificationResponse findById(Long id) throws Exception {

        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(id);

        if (notificationEntityOptional.isPresent()) {
            log.info("Notification found!!!");
            return modelMapper.map(notificationEntityOptional.get(), NotificationResponse.class);
        }

        log.error("Notification not found :-(");

        return null;
    }

    @Override
    public List<NotificationResponse> findAll() throws Exception {
        return List.of();
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        return false;
    }
}
