package com.aruiz.user.notification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class NotificationEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String content;
    private LocalDateTime creationDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private NotificationTypeEntity notificationType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity destinationUser;

}
