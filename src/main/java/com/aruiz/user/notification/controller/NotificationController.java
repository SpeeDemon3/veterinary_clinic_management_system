package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/send/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.isOwner(#email)")
    public ResponseEntity<String> sendNotification(@PathVariable String email, @RequestBody String message) {

        try {
            notificationService.sendNotification(message, email);
            return ResponseEntity.ok("The notification was sent correctly to -> " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
