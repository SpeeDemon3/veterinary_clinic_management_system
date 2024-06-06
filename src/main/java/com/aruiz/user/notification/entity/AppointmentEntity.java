package com.aruiz.user.notification.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dateOfAppointment;
    private String appointmentTime;
    private String description;

    @ManyToOne
    @JoinColumn(name="veterinarian_id")
    private UserEntity veterinarian;

    @ManyToOne
    @JoinColumn(name="pet_id")
    private PetEntity pet;

}
