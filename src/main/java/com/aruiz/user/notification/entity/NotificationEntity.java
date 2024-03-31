package com.aruiz.user.notification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime creationDate;

    @OneToOne(mappedBy = "notification", cascade = CascadeType.ALL)
    private NotificationTypeEntity notificationType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity destinationUser;

}
