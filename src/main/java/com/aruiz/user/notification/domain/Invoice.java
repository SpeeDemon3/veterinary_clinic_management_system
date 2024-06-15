package com.aruiz.user.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents an Invoice domain in the system.
 *
 * @author Antonio Ruiz = speedemon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    private Long id;
    private String invoiceNumber;
    private Double totalPrice;
    private Owner client;
    private LocalDate dateOfIssue;
    private String state;
}
