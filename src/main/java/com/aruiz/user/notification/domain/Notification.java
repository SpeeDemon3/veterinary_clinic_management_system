package com.aruiz.user.notification.domain;

import com.aruiz.user.notification.entity.NotificationTypeEntity;
import com.aruiz.user.notification.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    private Long id;
    private String content;
    private LocalDateTime creationDate;

    private NotificationTypeEntity notificationType;

    private User destinationUser;
}
