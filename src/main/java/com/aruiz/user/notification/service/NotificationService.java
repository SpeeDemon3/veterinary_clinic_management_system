package com.aruiz.user.notification.service;

public interface NotificationService {

    void sendNotification(String message, String ownerEmail);
    void sendEmail(String destinationEmail, String message);

}
