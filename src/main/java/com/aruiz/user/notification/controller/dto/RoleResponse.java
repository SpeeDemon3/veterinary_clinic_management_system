package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {

    private Long id;
    private String name;
    private String description;
    private User user;
}
