package com.aruiz.user.notification.domain;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private Profile profile;
    private List<Notification> notifications;


}
