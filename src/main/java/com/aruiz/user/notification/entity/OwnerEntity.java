package com.aruiz.user.notification.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class OwnerEntity {
    /* ESTA CLASE SOLO ES PARA IDENTIFICAR AL DUEÑ0 DE LA MASCOTA, NO ES NECESARIO APLICAR NINGUN
    TIPO DE ROL PORQUE NO VA A ENTRAR EN LA APLICACION, tampoco le hace falta imagen
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String dni;

    @Column(length = 13)
    private String phoneNumber;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PetEntity> pets;
}
