package com.aruiz.user.notification.domain;

import com.aruiz.user.notification.entity.PetEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Owner {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String dni;
    private String phoneNumber;
    private List<PetEntity> pets;

}
