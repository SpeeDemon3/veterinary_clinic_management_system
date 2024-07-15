package com.aruiz.user.notification.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Represents an invoice entity in the system.
 *
 * @author Antonio Ruiz = speedemon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    @Column(columnDefinition = "DOUBLE")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name="client_id")
    @ToString.Exclude
    private OwnerEntity client;

    @Column(columnDefinition = "DATE")
    private LocalDate dateOfIssue;

    private String state;


}
