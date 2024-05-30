package com.aruiz.user.notification.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String dni;
    private String phoneNumber;
    private String img;
    private String birthdate;
    //private Long role;
}
