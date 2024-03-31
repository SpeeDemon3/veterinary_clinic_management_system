package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.domain.Profile;
import com.aruiz.user.notification.domain.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private Long role;
    private Long profile;
}
