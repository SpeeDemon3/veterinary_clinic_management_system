package com.aruiz.user.notification.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity veterinarian;

    /*
    // Crear una clase OWNER que herede de userentity
    private String nameOwner;
    private String lastNameOwner;
    private String dni;

     */
    private String identificationCode;
    private String name;
    private String description;
    private String vaccinationData;

    @Column(columnDefinition = "LONGTEXT")
    private String img;

    private String birthdate;
    private String medication;
}
