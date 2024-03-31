package com.aruiz.user.notification.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String img;
    private String birthdate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
