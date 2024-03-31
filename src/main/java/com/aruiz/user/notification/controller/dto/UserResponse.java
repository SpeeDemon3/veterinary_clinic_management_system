package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.domain.Notification;
import com.aruiz.user.notification.domain.Profile;
import com.aruiz.user.notification.domain.Role;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private Profile profile;
    private List<Notification> notifications;
}
