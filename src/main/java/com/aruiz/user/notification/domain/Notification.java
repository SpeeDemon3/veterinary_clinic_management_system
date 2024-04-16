package com.aruiz.user.notification.domain;

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
    
    private User destinationUser;
}
