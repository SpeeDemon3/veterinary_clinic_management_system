package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.domain.Pet;
import com.aruiz.user.notification.domain.User;
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
    private User veterinarian;
    private Pet pet;
}
