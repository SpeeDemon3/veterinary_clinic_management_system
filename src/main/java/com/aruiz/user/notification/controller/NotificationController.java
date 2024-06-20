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

    /**
     * Endpoint to send a notification to a specified email.
     * This endpoint is secured such that only users with the 'ROLE_ADMIN' role or the owner of the email can access it.
     *
     * @param email The email address to which the notification will be sent.
     * @param message The message to be included in the notification.
     * @return ResponseEntity with a status of 200 OK if the notification was sent successfully, or 500 Internal Server Error if there was an error.
     */
    @PostMapping("/send/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityService.isOwner(#email)")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<String> sendNotification(@PathVariable String email, @RequestBody String message) {

        try {
            notificationService.sendNotification(message, email);
            return ResponseEntity.ok("The notification was sent correctly to -> " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
