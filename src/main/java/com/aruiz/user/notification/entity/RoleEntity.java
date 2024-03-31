package com.aruiz.user.notification.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RoleEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
