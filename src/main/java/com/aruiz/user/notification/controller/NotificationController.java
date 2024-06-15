package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.NotificationRequest;
import com.aruiz.user.notification.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling notification-related HTTP requests.
 *
 * @author Antonio Ruiz
 */
@RestController
@RequestMapping("/api/notify")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    @PostMapping("/add/{id}")
    public ResponseEntity<?> addNotify(@RequestBody NotificationRequest notificationRequest, @PathVariable Long id) {

        try {
            return ResponseEntity.ok(notificationService.save(notificationRequest, id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
