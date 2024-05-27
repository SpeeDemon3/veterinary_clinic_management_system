package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.entity.UserEntity;

public class PetRequestUpdate {
    private UserEntity owner;
    private String identificationCode;
    private String name;
    private String description;
    private String vaccinationData;
    private String img;
    private String birthdate;
    private String medication;
}
