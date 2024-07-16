package com.aruiz.user.notification.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity class representing an owner/client.
 *
 * @author Antonio Ruiz Benito
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class OwnerEntity {

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

    @OneToMany(mappedBy = "client")
    @ToString.Exclude
    private List<InvoiceEntity> invoices;

    //@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    @ToString.Exclude
    private List<PetEntity> pets;
}
