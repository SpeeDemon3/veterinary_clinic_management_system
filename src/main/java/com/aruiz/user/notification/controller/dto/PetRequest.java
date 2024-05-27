package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetRequest {
    private UserEntity owner;
    private String name;
    private String description;
    private String birthdate;
}
