package com.aruiz.user.notification.domain;

import com.aruiz.user.notification.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Pet domain in the system.
 *
 * @author Antonio Ruiz = speedemon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    private Long id;
    private UserEntity veterinarian;
    private String identificationCode;
    private String name;
    private String description;
    private String vaccinationData;
    private String img;
    private String birthdate;
    private String medication;
}
