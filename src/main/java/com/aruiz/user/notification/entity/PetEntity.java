package com.aruiz.user.notification.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a pet entity in the system.
 *
 * @author Antonio Ruiz = speedemon
 */
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
    @ToString.Exclude
    private UserEntity veterinarian;

    @ManyToOne
    @JoinColumn(name="owner_id")
    @ToString.Exclude
    private OwnerEntity owner;

    private String identificationCode;
    private String name;
    private String description;
    private String vaccinationData;

    @Column(columnDefinition = "LONGTEXT")
    private String img;

    private String birthdate;
    private String medication;
}
