package com.aruiz.user.notification.service;

import com.aruiz.user.notification.controller.dto.NotificationRequest;
import com.aruiz.user.notification.controller.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    NotificationResponse save(NotificationRequest notificationRequest, Long destinationUserId) throws Exception;

    NotificationResponse findById(Long id) throws Exception;

    List<NotificationResponse> findAll() throws Exception;

    boolean deleteById(Long id) throws Exception;

}
