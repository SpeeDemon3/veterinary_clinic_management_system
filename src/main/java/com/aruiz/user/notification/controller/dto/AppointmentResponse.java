package com.aruiz.user.notification.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;
    private String dateOfAppointment;
    private String appointmentTime;
    private String description;
    private Long veterinarian;
    private String nameVeterinarian;
    private Long pet;
    private String namePet;
}
