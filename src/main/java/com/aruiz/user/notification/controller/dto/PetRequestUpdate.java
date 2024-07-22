package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.domain.Owner;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetRequestUpdate {
    //private Long veterinarian;
    private UserEntity veterinarian;
    private OwnerEntity owner;
    private String identificationCode;
    private String name;
    private String description;
    private String vaccinationData;
    private String img;
    private String birthdate;
    private String medication;
}
