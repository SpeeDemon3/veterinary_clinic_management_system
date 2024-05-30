package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.domain.Notification;
import com.aruiz.user.notification.domain.Pet;
import com.aruiz.user.notification.domain.Role;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String dni;
    private String phoneNumber;
    private String img;
    private String birthdate;
    private Role role;
    private List<Notification> notifications;
    private List<Pet> pets;

}
