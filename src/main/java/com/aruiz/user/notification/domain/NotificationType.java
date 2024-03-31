package com.aruiz.user.notification.domain;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationType {
    private Long id;

    private String name;

    private String description;
    private Notification notification;

}
