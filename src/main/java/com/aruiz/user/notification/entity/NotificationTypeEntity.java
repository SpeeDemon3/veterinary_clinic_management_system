package com.aruiz.user.notification.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class NotificationTypeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    @OneToOne
    @JoinColumn(name = "notification_id")
    private NotificationEntity notification;

}
