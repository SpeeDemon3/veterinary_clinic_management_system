package com.aruiz.user.notification.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestUpdate {
    private String name;
    private String email;
    private String password;
    private Long role;
    private Long profile;

}
