package com.aruiz.user.notification.controller.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private String dni;
    private String phoneNumber;
    private String img;
    private String birthdate;
}
