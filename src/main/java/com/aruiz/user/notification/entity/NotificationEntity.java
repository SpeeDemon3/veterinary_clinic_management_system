package com.aruiz.user.notification.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notifications")
public class NotificationEntity {

    @Id
    private String id;

    private String message;
    private LocalDateTime creationDate;
    private String email;


    /*
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity destinationUser;
    */
}
