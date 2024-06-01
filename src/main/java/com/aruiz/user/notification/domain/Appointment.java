package com.aruiz.user.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointment {

    private Long id;
    private String dateOfAppointment;
    private String appointmentTime;
    private String description;
    private User veterinarian;
    private Pet pet;

}
