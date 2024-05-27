package com.aruiz.user.notification.domain;

import com.aruiz.user.notification.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    private Long id;
    private UserEntity owner;
    private String name;
    private String img;
}
