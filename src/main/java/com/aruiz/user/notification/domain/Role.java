package com.aruiz.user.notification.domain;

import lombok.*;

import java.util.List;

/**
 * Represents a Role domain in the system.
 *
 * @author Antonio Ruiz = speedemon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    private Long id;
    private String name;
    private String description;
    private List<User> user;

}
