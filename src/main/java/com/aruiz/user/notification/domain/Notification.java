package com.aruiz.user.notification.domain;

import com.aruiz.user.notification.entity.NotificationTypeEntity;
import com.aruiz.user.notification.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
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
