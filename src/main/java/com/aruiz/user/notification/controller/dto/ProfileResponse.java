package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private Long id;
    private String img;
    private String birthdate;
    private User user;
}
